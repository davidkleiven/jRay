package com.github.jray;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FresnelAmplitudesTest
{
    @Test
    public void testSumRuleSpol()
    {
        double n1 = 1.0;
        double n2 = 1.5;

        FresnelAmplitudes fresnel = new FresnelAmplitudes(n1, n2);

        double angle = 0.46; // Red

        double rs = fresnel.rs(angle);
        double ts = fresnel.ts(angle);
        assertEquals(ts, 1.0+rs, 1E-6);
    }

    @Test
    public void testSumRulePpol()
    {
        double n1 = 1.0;
        double n2 = 1.5;

        FresnelAmplitudes fresnel = new FresnelAmplitudes(n1, n2);

        double angle = 0.46; // Red

        double rp = fresnel.rp(angle);
        double tp = fresnel.tp(angle);
        assertEquals(n2*tp/n1, 1.0+rp, 1E-6);
    }

    @Test
    public void testTotalInternalReflection()
    {
        double n1 = 1.5;
        double n2 = 1.0;

        FresnelAmplitudes fresnel = new FresnelAmplitudes(n1, n2);

        double angle = 1.4; // Rad

        double rs = fresnel.rs(angle);
        double ts = fresnel.ts(angle);
        double rp = fresnel.rp(angle);
        double tp = fresnel.tp(angle);

        assertEquals(1.0, rs, 1E-6);
        assertEquals(1.0, rp, 1E-6);
        assertEquals(0.0, ts, 1E-6);
        assertEquals(0.0, tp, 1E-6);
    }
}