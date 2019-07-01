package com.github.jray;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.ArrayList;

enum TerminationCause{
    NO_ELEMENTS_IN_RAY_PATH,
    MAX_BOUNCES_REACHED,
    NO_BEAMS,
    MIN_AMPLITUDE_REACED,
};

public class BeamPropagator
{
    private LinkedList<Ray> rays = new LinkedList<Ray>();
    private LinkedList<Boolean> propagated = new LinkedList<Boolean>();
    public int maxBounces = 10;
    public double minAmplitude = 1E-3;

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
        nextHit.scatteringMedium = geo.getMedium(ray.mediumId);
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
                        nextHit.time = time;

                        if (medium.getID() == ray.mediumId){
                            // Ray it another wall of the object
                            // we assume that we enter vacuum
                            nextHit.newRefractiveIndex = 1.0;
                            nextHit.scatteringMedium = geo.getMedium(0);
                        }
                        else{
                            // We hit another object
                            nextHit.newRefractiveIndex = medium.getRefractiveIndex();
                            nextHit.scatteringMedium = medium;
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

        // Make a deep copy of the incident direction
        Vector incDirection = new Vector(0.0, 0.0, 0.0);
        incDirection.set(ray.getDirection());
        ray.opticalPathLength += scatInfo.currentRefractiveIndex*scatInfo.time;

        // Update monitor if present
        scatInfo.scatteringMedium.updateMonitor(scatInfo.element, ray);

        double angle = Math.acos(normal.dot(incDirection));

        if (angle > Math.PI/2.0){
            angle = Math.PI - angle;
        }

        FresnelAmplitudes fresnel = new FresnelAmplitudes(scatInfo.currentRefractiveIndex, scatInfo.newRefractiveIndex);

        double rp = fresnel.rp(angle);
        double rs = fresnel.rs(angle);
        double ts = fresnel.ts(angle);
        double tp = fresnel.tp(angle);

        Vector amp = ray.getAmplitude();
        Vector incPlaneNormal = incDirection.cross(normal);
        
        // At normal incidence the cross product is zero
        // We just use the amplitude vector as normal
        if (incDirection.isParallel(normal)){
            incPlaneNormal.set(ray.getAmplitude());
        }

        incPlaneNormal.idivide(incPlaneNormal.length());
        double normalComp = incPlaneNormal.dot(amp);
        Vector normalAmp = incPlaneNormal.mult(normalComp);
        Vector parallelAmp = amp.subtract(normalAmp);
        double normDirAmp = normal.dot(incDirection);
        Vector normalDir = normal.mult(normDirAmp);
        Vector twoNormalDir = normalDir.mult(2.0);

        // Reflected ray
        Vector normalAmpRef = normalAmp.mult(rs);
        Vector parAmpRef = parallelAmp.mult(rp);
        Vector refAmp = normalAmpRef.add(parAmpRef);
        Vector refDir = incDirection.subtract(twoNormalDir);

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
        transRay.opticalPathLength = ray.opticalPathLength;
        double sinThetaT = fresnel.sinTransmissionAngle(angle);

        if (Math.abs(sinThetaT) > 1.0){
            return;
        }

        double thetaT = Math.asin(sinThetaT);
        double costThetaT = Math.cos(thetaT);
        Vector transDir = normal.mult(costThetaT);
        Vector rPar = incDirection.subtract(normalDir);

        if (!incDirection.isParallel(normal)){
            rPar.idivide(rPar.length());
        }

        System.out.println(rPar.displayProfile());
        rPar.imult(sinThetaT);
        
        // Make sure sign of inner product is preserved
        boolean origPositive = (normDirAmp > 0.0);
        boolean newPositive = (transDir.dot(normal) > 0.0);

        if (origPositive != newPositive){
            transDir.imult(-1.0);
        }

        // Determine the sign of the parallel component
        if (incDirection.dot(rPar) < 0.0){
            rPar.imult(-1.0);
        }
        transDir.iadd(rPar);
        transRay.setDirection(transDir.getX(), transDir.getY(), transDir.getZ());
        transRay.position.set(scatInfo.intersectionPoint);
        smallDistance = transRay.getDirection().mult(eps);
        transRay.position.iadd(smallDistance);
        transRay.mediumId = scatInfo.scatteringMedium.getID();

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
            if (ray.amplitude() < minAmplitude){
                return TerminationCause.MIN_AMPLITUDE_REACED;
            }

            this.scatter(nextElement, ray);
            numBounces += 1;
            if (numBounces >= this.maxBounces){
                return TerminationCause.MAX_BOUNCES_REACHED;
            }
            nextElement = nextElementHit(geo, ray);
        }
        return TerminationCause.NO_ELEMENTS_IN_RAY_PATH;
    }

    /**
     * To avoid consuming too much memory clear rays that are finished
     */
    public void clearFinishedRays()
    {
        ArrayList<Ray> removeRays = new ArrayList<Ray>();
        ArrayList<Boolean> removePropagated = new ArrayList<Boolean>();

        Iterator<Ray> rayIter = rays.iterator();
        for (Iterator<Boolean> i=propagated.iterator(); i.hasNext();){
            Boolean finished = i.next();
            Ray ray = rayIter.next();

            if (finished){
                removeRays.add(ray);
                removePropagated.add(finished);
            }
        }
        rays.removeAll(removeRays);
        propagated.removeAll(removePropagated);
    }
}