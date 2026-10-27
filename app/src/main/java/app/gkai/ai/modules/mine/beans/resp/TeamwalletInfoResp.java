package app.gkai.ai.modules.mine.beans.resp;

import lombok.Data;

@Data
public class TeamwalletInfoResp {


    /**
     * availableBalance : 0
     * currency : 
     * frozenBalance : 0
     * id : 0
     * memberId : 0
     * totalBalance : 0
     * version : 0
     */

    private Number availableBalance;
    private String currency;
    private Number    frozenBalance;
    private Number    id;
    private Number    memberId;
    private Number    totalBalance;
    private Number    version;

    
}
