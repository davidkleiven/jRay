package com.github.jray;

import java.util.ArrayList;
import java.util.Map;

enum TerminationCause{
    NO_ELEMENTS_IN_RAY_PATH,
    MAX_BOUNCES_REACHED,
    NO_BEAMS
};

public class BeamPropagator
{
    private ArrayList<Ray> rays = new ArrayList<Ray>();
    private ArrayList<Boolean> propagated = new ArrayList<Boolean>();
    public int maxBounces = 10;

    public void addRay(Ray ray)
    {
        rays.add(ray);
        propagated.add(false);
    }

    public Ray getRay(int id){
        return rays.get(id);
    }

    private int grabAvailableRay()
    {
        for (int i=0;i<propagated.size();i++)
        {
            if (!propagated.get(i)){
                propagated.set(i, true);
                return i;
            }
        }
        return -1;
    }

    public int totalNumberOfRays()
    {
        return rays.size();
    }

    public int numPropagated()
    {
        int counter = 0;
        for (Boolean value : propagated)
        {
            if (value)
            {
                counter += 1;
            }
        }
        return counter;
    }

    /**
     * Calculate ID of the element with shortest time to hit
     */
    private ScatteringInfo nextElementHit(Geometry geo, Ray ray)
    {
        ScatteringInfo nextHit = new ScatteringInfo();
        nextHit.currentRefractiveIndex = geo.getMedium(ray.mediumId).getRefractiveIndex();
        nextHit.scatteringMediumId = 0;
        nextHit.element = -1;

        double timeToHit = 0.0;

        for (Map.Entry<Integer,PhysicalMedium> entry : geo.allMediums().entrySet()){
            PhysicalMedium medium = entry.getValue();

            if (medium.mesh == null){
                continue;
            }
            for (int i=0;i<medium.mesh.numElements();i++){
                if (medium.mesh.rayIntersectsElement(ray, i))
                {
                    double time = medium.mesh.timeUntilHit(ray, i);
                    if ((nextHit.element == -1) || (time < timeToHit))
                    {
                        nextHit.element = i;
                        nextHit.mesh = medium.mesh;
                        timeToHit = time;
                        Vector dist = ray.getDirection().mult(timeToHit);
                        nextHit.intersectionPoint.iadd(ray.position);
                        nextHit.intersectionPoint.iadd(dist);

                        if (medium.getID() == ray.mediumId){
                            // Ray it another wall of the object
                            // we assume that we enter vacuum
                            nextHit.newRefractiveIndex = 1.0;
                            nextHit.scatteringMediumId = 0;
                        }
                        else{
                            // We hit another object
                            nextHit.newRefractiveIndex = medium.getRefractiveIndex();
                            nextHit.scatteringMediumId = medium.getID();
                        }

                    }
                }
            }
        }
        return nextHit;
    }

    private void scatter(ScatteringInfo scatInfo, Ray ray)
    {
        Vector normal = scatInfo.mesh.normal(scatInfo.element);

        double angle = Math.acos(normal.dot(ray.getDirection()));

        if (angle > Math.PI/2.0){
            angle = Math.PI - angle;
        }

        FresnelAmplitudes fresnel = new FresnelAmplitudes(scatInfo.currentRefractiveIndex, scatInfo.newRefractiveIndex);

        double rp = fresnel.rp(angle);
        double rs = fresnel.rs(angle);
        double ts = fresnel.ts(angle);
        double tp = fresnel.tp(angle);

        Vector amp = ray.getAmplitude();
        double normalComp = normal.dot(amp);
        Vector normalAmp = normal.mult(normalComp);
        Vector parallelAmp = amp.subtract(normalAmp);
        double normDirAmp = normal.dot(ray.getDirection());
        Vector normalDir = normal.mult(normDirAmp);
        normalDir.imult(2.0);


        // Reflected ray
        Vector normalAmpRef = normalAmp.mult(rs);
        Vector parAmpRef = parallelAmp.mult(rp);
        Vector refAmp = normalAmpRef.add(parAmpRef);
        Vector refDir = ray.getDirection().subtract(normalDir);

        ray.setDirection(refDir.getX(), refDir.getY(), refDir.getZ());
        ray.setAmplitude(refAmp.getX(), refAmp.getY(), refAmp.getZ());
        ray.position.set(scatInfo.intersectionPoint);

        // Propagate a small distance along the new direction
        double eps = 1E-6;
        Vector smallDistance = ray.getDirection().mult(eps);
        ray.position.iadd(smallDistance);


        // Transmitted ray
        Vector normalAmpTrans = normalAmp.mult(ts);
        Vector parAmpTrans = parallelAmp.mult(tp);
        Vector transAmp = normalAmpTrans.add(parAmpTrans);
        Ray transRay = new Ray();
        transRay.setAmplitude(transAmp.getX(), transAmp.getY(), transAmp.getZ());
        double sinThetaT = fresnel.sinTransmissionAngle(angle);

        if (Math.abs(sinThetaT) > 1.0){
            return;
        }

        double thetaT = Math.asin(sinThetaT);
        double costThetaT = Math.cos(thetaT);
        Vector transDir = normal.mult(costThetaT);
        Vector rPar = ray.getDirection().subtract(normalDir);
        rPar.idivide(rPar.length());
        rPar.imult(sinThetaT);
        
        // Make sure sign of inner product is preserved
        boolean origPositive = (normDirAmp > 0.0);
        boolean newPositive = (transDir.dot(normal) > 0.0);

        if (origPositive != newPositive){
            transDir.imult(-1.0);
        }

        // Determine the sign of the parallel component
        if (ray.getDirection().dot(rPar) < 0.0){
            rPar.imult(-1.0);
        }
        transDir.iadd(rPar);
        transRay.setDirection(transDir.getX(), transDir.getY(), transDir.getZ());
        transRay.position.set(scatInfo.intersectionPoint);
        smallDistance = transRay.getDirection().mult(eps);
        transDir.iadd(smallDistance);
        transRay.mediumId = scatInfo.scatteringMediumId;

        // Add the ray to the pool of rays
        addRay(transRay);
    }

    public TerminationCause propagateNextActive(Geometry geo)
    {
        int rayId = this.grabAvailableRay();

        if (rayId == -1){
            return TerminationCause.NO_BEAMS;
        }

        Ray ray = rays.get(rayId);

        ScatteringInfo nextElement = nextElementHit(geo, ray);
        int numBounces = 0;
        while (nextElement.element != -1)
        {
            this.scatter(nextElement, ray);
            if (numBounces > this.maxBounces){
                return TerminationCause.MAX_BOUNCES_REACHED;
            }
            nextElement = nextElementHit(geo, ray);
            numBounces += 1;
        }
        return TerminationCause.NO_ELEMENTS_IN_RAY_PATH;
    }
}