package com.github.jray;

import java.util.ArrayList;

public class BeamPropagator
{
    private ArrayList<Ray> rays = new ArrayList<Ray>();
    private ArrayList<Boolean> propagated = new ArrayList<Boolean>();
    private int activeRayId = 0;

    public void addRay(Ray ray)
    {
        rays.add(ray);
        propagated.add(false);
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

    /**
     * Calculate ID of the element with shortest time to hit
     */
    private ScatteringInfo nextElementHit(Geometry geo, Ray ray)
    {
        ScatteringInfo nextHit = new ScatteringInfo();
        nextHit.currentRefractiveIndex = geo.mediums.get(ray.mediumId).getRefractiveIndex();
        nextHit.scatteringMediumId = 0;

        double timeToHit = 0.0;

        for (PhysicalMedium medium : geo.mediums){
            for (int i=0;i<medium.mesh.numElements();i++){
                if (medium.mesh.rayIntersectsElement(ray, i))
                {
                    double time = medium.mesh.timeUntilHit(ray, i);
                    if ((nextHit.element == -1) || (time < timeToHit))
                    {
                        nextHit.element = i;
                        nextHit.mesh = medium.mesh;
                        timeToHit = time;

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
        Vector normalAmp = 
    }

    public void propagateNextActive(Geometry geo)
    {
        int rayId = this.grabAvailableRay();

        if (rayId == -1){
            return;
        }

        Ray ray = rays.get(rayId);

        ScatteringInfo nextElement = nextElementHit(geo, ray);
        while (nextElement.element != -1)
        {
            this.scatter(nextElement, ray);
        }
    }
}