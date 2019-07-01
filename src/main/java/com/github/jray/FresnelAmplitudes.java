package com.github.jray;

public class FresnelAmplitudes{
    private double refractiveFrom;
    private double refractiveTo;

    public FresnelAmplitudes(double nFrom, double nTo)
    {
        refractiveFrom = nFrom;
        refractiveTo = nTo;
    }

    public double sinTransmissionAngle(double inc_angle)
    {
        double sinTheta = this.refractiveFrom*Math.sin(inc_angle)/this.refractiveTo;
        return sinTheta;
    }

    public double rp(double angle)
    {
        double sinThetaT = this.sinTransmissionAngle(angle);
        if (Math.abs(sinThetaT) > 1.0){
            return 1.0;
        }

        double thetaT = Math.asin(sinThetaT);

        // Cosine of needed angles
        double cosT = Math.cos(thetaT);
        double cosI = Math.cos(angle);

        return (refractiveTo*cosI - refractiveFrom*cosT)/(refractiveTo*cosI + refractiveFrom*cosT);
    }

    public double rs(double angle)
    {
        double sinThetaT = this.sinTransmissionAngle(angle);

        if (Math.abs(sinThetaT) > 1.0){
            return 1.0;
        }

        double thetaT = Math.asin(sinThetaT);

        // Cosine of needed angles
        double cosT = Math.cos(thetaT);
        double cosI = Math.cos(angle);
        return (refractiveFrom*cosI - refractiveTo*cosT)/(refractiveFrom*cosI + refractiveTo*cosT);
    }

    public double tp(double angle)
    {
        double sinThetaT = this.sinTransmissionAngle(angle);

        if (Math.abs(sinThetaT) > 1.0){
            return 0.0;
        }

        double thetaT = Math.asin(sinThetaT);

        // Cosine of needed angles
        double cosT = Math.cos(thetaT);
        double cosI = Math.cos(angle);
        return 2*refractiveFrom*cosI/(refractiveTo*cosI + refractiveFrom*cosT);
    }

    public double ts(double angle)
    {
        double sinThetaT = this.sinTransmissionAngle(angle);

        if (Math.abs(sinThetaT) > 1.0){
            return 0.0;
        }

        double thetaT = Math.asin(sinThetaT);

        // Cosine of needed angles
        double cosT = Math.cos(thetaT);
        double cosI = Math.cos(angle);
        return 2*refractiveFrom*cosI/(refractiveFrom*cosI + refractiveTo*cosT);
    }


}