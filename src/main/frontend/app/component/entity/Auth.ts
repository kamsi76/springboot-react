export namespace Auth {
    export type User = {
        userId: string;
        passwd: string;
    }

    export type authResponse = {
        userInfo: any;
        accessToken: string;
        refreshToken: string;
        resultCode: string | number;
        message: string;
    }
}