package utils.entities;

import java.text.DecimalFormat;
import java.util.logging.Level;

import utils.log.Log;

public class OCSpace {

    private String type;
    private String id;
    private String name;
    private String description;
    private String owner;
    private long quota;

    public OCSpace(){
    };

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuota(String unit) {
        return transformQuota(quota, unit);
    }

    public void setQuota(long quota) {
        this.quota = quota;
    }

    private String transformQuota(long quota, String unit) {
        if (quota == 0) {
            return "No restriction";
        } else {
            Log.log(Level.FINE, "TRANSFORM QUOTA");
            Log.log(Level.FINE, "Quota in bytes: " + quota);
            switch (unit) {
                case "MB" -> {
                    double mb = quota / 1_000_000.0;
                    DecimalFormat dfMB = new DecimalFormat("0.######");
                    Log.log(Level.FINE, "Quota in MB: " + dfMB.format(mb));
                    return dfMB.format(mb);
                }
                case "GB" -> {
                    double gb = quota / 1_000_000_000.0;
                    DecimalFormat dfGB = new DecimalFormat("0.######");
                    Log.log(Level.FINE, "Quota in TB: " + dfGB.format(gb));
                    return dfGB.format(gb);
                }
                case "TB" -> {
                    double tb = quota / 1_000_000_000_000.0;
                    DecimalFormat dfTB = new DecimalFormat("0.######");
                    Log.log(Level.FINE, "Quota in TB: " + dfTB.format(tb));
                    return dfTB.format(tb);
                }
            }
            return "No restriction";
        }
    }

}
