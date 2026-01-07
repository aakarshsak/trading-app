package com.sinha.trading_app.auth_service.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RolePermissions {

    @JsonProperty("canTrade")
    private Boolean canTrade;

    @JsonProperty("canWithdraw")
    private Boolean canWithdraw;

    @JsonProperty("canManageUsers")
    private Boolean canManageUsers;

    @JsonProperty("canViewReports")
    private Boolean canViewReports;

    @JsonProperty("canModifyOrders")
    private Boolean canModifyOrders;

    @JsonProperty("canAccessAPI")
    private Boolean canAccessAPI;


    public Boolean hasPermission(String permission) {
        switch (permission.toLowerCase()) {
            case "trade":
                return canTrade;
            case "withdraw":
                return canWithdraw;
            case "manageusers":
                return canManageUsers;
            case "viewreports":
                return canViewReports;
            case "modifyorders":
                return canModifyOrders;
            case "accessapi":
                return canAccessAPI;
            default:
                return false;
        }
    }
}
