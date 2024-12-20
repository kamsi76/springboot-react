import { userInfo } from "./UserInfo";

export interface requestUser {
    userId: string;
    passwd: string;
}

export interface authResponse {
    userInfo: userInfo;
    accessToken: string;
    refreshToken: string;
    resultCode: string | number;
    message: string;
}