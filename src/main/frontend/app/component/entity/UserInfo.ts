export namespace UserInfo {

    export type User = {
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
    
}
