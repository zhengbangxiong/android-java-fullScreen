package app.gkai.ai.modules.mine.beans.resp;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户认证信息
 */
@Data
public class AuthindentityResp implements Serializable {


        /**
         * auditHistory :
         * auditTime :
         * auditor :
         * birthday :
         * city :
         * familyName :
         * id : 0
         * idBackImg :
         * idBeginDate :
         * idEndDate :
         * idFrontImg :
         * idHandheldImg :
         * idNumber :
         * idStatus : 0
         * lastSubmitTime :
         * nationality :
         * province :
         * rejectReason :
         * sex :
         */

        private String auditHistory;
        private String auditTime;
        private String auditor;
        private String birthday;
        private String city;
        private String familyName;
        private int    id;
        private String idBackImg;
        private String idBeginDate;
        private String idEndDate;
        private String idFrontImg;
        private String idHandheldImg;
        private String idNumber;
        private int    idStatus;
        private String lastSubmitTime;
        private String nationality;
        private String province;
        private String rejectReason;
        private String sex;

}
