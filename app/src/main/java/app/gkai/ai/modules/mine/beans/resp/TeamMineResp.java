package app.gkai.ai.modules.mine.beans.resp;

import lombok.Data;

@Data
public class TeamMineResp {


    /**
     * sumTeamRebate : 0
     * sumYestodayMemberNum : 0
     * totalTeamNum : 0
     * totalZhiTuiNum : 0
     * yestodayZhiTuiNum : 0
     */

    private String sumTeamRebate;
    private int sumYestodayMemberNum;
    private int totalTeamNum;
    private int totalZhiTuiNum;
    private int yestodayZhiTuiNum;
    private int jibie;

    private int showWallet;
    
}
