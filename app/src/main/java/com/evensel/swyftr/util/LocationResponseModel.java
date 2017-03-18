
package com.evensel.swyftr.util;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "status",
    "message",
    "details"
})
public class LocationResponseModel implements Parcelable {

    @JsonProperty("status")
    private String status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("details")
    private List<Double> details = null;

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("details")
    public List<Double> getDetails() {
        return details;
    }

    @JsonProperty("details")
    public void setDetails(List<Double> details) {
        this.details = details;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeString(this.message);
        dest.writeList(this.details);
    }

    public LocationResponseModel() {
    }

    protected LocationResponseModel(Parcel in) {
        this.status = in.readString();
        this.message = in.readString();
        this.details = new ArrayList<Double>();
        in.readList(this.details, Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<LocationResponseModel> CREATOR = new Parcelable.Creator<LocationResponseModel>() {
        @Override
        public LocationResponseModel createFromParcel(Parcel source) {
            return new LocationResponseModel(source);
        }

        @Override
        public LocationResponseModel[] newArray(int size) {
            return new LocationResponseModel[size];
        }
    };
}
