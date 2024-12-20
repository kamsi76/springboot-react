import { AxiosResponse } from "axios";
import AxiosJwtInstance from "../../common/instance/AxiosJwtInstance";
import { requestUser, authResponse } from "../../entity/Auth";
import { userInfo } from "../../entity/UserInfo";
import { Token } from "../../entity/Token";

class AuthService {

    async signup(
        userInfo: userInfo
    ): Promise<AxiosResponse<boolean>> {
        return await AxiosJwtInstance.post('/api/v1/auth/signup', userInfo);
    };

    async signin(
        userInfo: requestUser
    ): Promise<AxiosResponse<Token>> {
        return await AxiosJwtInstance.post('/api/v1/auth/signin', userInfo);
    };

    async mypage(): Promise<AxiosResponse<userInfo>> {
        return await AxiosJwtInstance.get('/api/v1/mypage');
    };

    async signout(): Promise<AxiosResponse<authResponse>> {
        const token = localStorage.getItem('accessToken');
        return await AxiosJwtInstance.post('/api/v1/auth/signout', {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
    }

    async authroize(
        path: string
    ): Promise<AxiosResponse<boolean>> {
        const token = localStorage.getItem('accessToken');
        return await AxiosJwtInstance.post('/api/v1/auth/authroize', JSON.stringify({ path }), {
            headers: {
                Authorization: `Bearer ${token}`
            }
        });
    }
}

export const authService = new AuthService();

