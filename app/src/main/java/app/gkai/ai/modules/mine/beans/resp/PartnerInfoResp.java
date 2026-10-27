package app.gkai.ai.modules.mine.beans.resp;

import lombok.Data;

@Data
public class PartnerInfoResp {


    /**
     * agencyAgreement :
     * condition :
     * createTime :
     * id : 0
     * newRebate : 0
     * partnerName :
     * selfBuyTb : 0
     * teamRebate : 0
     * teamTb : 0
     */

    private String agencyAgreement;
    private String condition;
    private String createTime;
    private int    id;
    private Number    newRebate;
    private String partnerName;
    private Number    selfBuyTb;
    private Number    teamRebate;
    private Number    teamTb;
    private Number status;

    private Number currentPartnerId;


}
