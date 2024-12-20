import { menuInfo } from "./MenuInfo";

export interface userInfo {
    userId: string;
    passwd: string;
    userNm: string;
    cttpc: string;
    email: string;
    registDttm: Date;
    updusrId: string;
    updtDttm: Date;

    token?: {
        accessToken: string;
        refreshToken: string;
    }
}
