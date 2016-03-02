package com.bnmla.advideos.Utilities;

/**
 * Created by nay on 3/2/16.
 */
public class VPAIDResponse {
    public final String medialUrl;
    public final String adParameters;

    public VPAIDResponse (String mediaUrl, String adParameters) {
        this.medialUrl = mediaUrl;
        this.adParameters = adParameters;
    }
}
