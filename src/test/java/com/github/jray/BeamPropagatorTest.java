package com.github.jray;

import static org.junit.Assert.assertEquals;

import org.junit.Test;


public class BeamPropagatorTest
{
    @Test
    public void testNormalIncidence()
    {
        GMSHReader reader = new GMSHReader();
        TriangleMesh mesh = reader.readGMSH("res/box.msh");

        PhysicalMedium glassBox = new PhysicalMedium(1.5);
        glassBox.mesh = mesh;

        Geometry geo = new Geometry();
        geo.addMedium(glassBox);

        Ray ray = new Ray();
        ray.position.set(0.5, 0.5, -10.0);
        ray.setDirection(0.0, 0.0, 1.0);
        ray.setAmplitude(1.0, 0.0, 0.0);

        BeamPropagator prop = new BeamPropagator();
        prop.addRay(ray);

        TerminationCause cause = prop.propagateNextActive(geo);

        // At this point we should have two rays
        assertEquals(2, prop.totalNumberOfRays());
        assertEquals(1, prop.numPropagated());
        assertEquals(TerminationCause.NO_ELEMENTS_IN_RAY_PATH, cause);
        assertEquals(0.0, ray.getDirection().dot(ray.getAmplitude()), 1E-6);

        Vector direction = ray.getDirection();
        assertEquals(0.0, direction.getX(), 1E-6);
        assertEquals(0.0, direction.getY(), 1E-6);
        assertEquals(-1.0, direction.getZ(), 1E-6);

        Vector amp = ray.getAmplitude();
        assertEquals(0.0, amp.getY(), 0.0);
        assertEquals(0.0, amp.getZ(), 0.0);
        assertEquals(-0.5/2.5, amp.getX(), 1E-6);

        Ray transmitted = prop.getRay(1);
        direction = transmitted.getDirection();
        assertEquals(0.0, direction.getX(), 1E-6);
        assertEquals(0.0, direction.getY(), 1E-6);
        assertEquals(1.0, direction.getZ(), 1E-6);

        amp = transmitted.getAmplitude();
        assertEquals(0.0, amp.getY(), 0.0);
        assertEquals(0.0, amp.getZ(), 0.0);
        assertEquals(2.0/2.5, amp.getX(), 1E-6);
    }

    @Test
    public void testObliqueIncidence()
    {
        GMSHReader reader = new GMSHReader();
        TriangleMesh mesh = reader.readGMSH("res/box.msh");

        PhysicalMedium glassBox = new PhysicalMedium(1.5);
        glassBox.mesh = mesh;

        Geometry geo = new Geometry();
        geo.addMedium(glassBox);

        Ray ray = new Ray();
        double angle = 0.4;
        ray.position.set(0.5, 0.5, -1.0);
        ray.setDirection(Math.sin(angle), 0.0, Math.cos(angle));
        ray.setAmplitude(0.0, 1.0, 0.0);

        BeamPropagator prop = new BeamPropagator();
        prop.addRay(ray);

        TerminationCause cause = prop.propagateNextActive(geo);

        // At this point we should have two rays
        assertEquals(2, prop.totalNumberOfRays());
        assertEquals(1, prop.numPropagated());
        assertEquals(TerminationCause.NO_ELEMENTS_IN_RAY_PATH, cause);
        assertEquals(0.0, ray.getDirection().dot(ray.getAmplitude()), 1E-6);

        Vector direction = ray.getDirection();
        assertEquals(Math.sin(angle), direction.getX(), 1E-6);
        assertEquals(0.0, direction.getY(), 1E-6);
        assertEquals(-Math.cos(angle), direction.getZ(), 1E-6);

        Vector amp = ray.getAmplitude();
        assertEquals(0.0, amp.getX(), 0.0);
        assertEquals(0.0, amp.getZ(), 0.0);
        
        FresnelAmplitudes fresnel = new FresnelAmplitudes(1.0, 1.5);
        double rs = fresnel.rs(angle);
        assertEquals(rs, amp.getY(), 1E-6);

        Ray transmitted = prop.getRay(1);
        double sinThetaT = fresnel.sinTransmissionAngle(angle);
        double thetaT = Math.asin(sinThetaT);
        direction = transmitted.getDirection();
        assertEquals(sinThetaT, direction.getX(), 1E-6);
        assertEquals(0.0, direction.getY(), 1E-6);
        assertEquals(Math.cos(thetaT), direction.getZ(), 1E-6);

        amp = transmitted.getAmplitude();
        assertEquals(0.0, amp.getX(), 0.0);
        assertEquals(0.0, amp.getZ(), 0.0);
        double ts = fresnel.ts(angle);
        assertEquals(ts, amp.getY(), 1E-6);

        // Try to remove the finished ones
        prop.clearFinishedRays();
        assertEquals(1, prop.totalNumberOfRays());
    }
}