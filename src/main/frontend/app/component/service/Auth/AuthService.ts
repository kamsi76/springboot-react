import { AxiosResponse } from "axios";
import AxiosJwtInstance from "../../common/instance/AxiosJwtInstance";
import { Auth } from "../../entity/Auth";
import { UserInfo } from "../../entity/UserInfo";
import { Token } from "../../entity/Token";

class AuthService {

    async signup(
        userInfo: UserInfo.User
    ): Promise<AxiosResponse<boolean>> {
        return await AxiosJwtInstance.post('/api/v1/auth/signup', userInfo);
    };

    async signin(
        userInfo: Auth.User
    ): Promise<AxiosResponse<Token.Token>> {
        return await AxiosJwtInstance.post('/api/v1/auth/signin', userInfo);
    };

    async mypage(): Promise<AxiosResponse<UserInfo.User>> {
        return await AxiosJwtInstance.get('/api/v1/mypage');
    };

    async signout(): Promise<AxiosResponse<Boolean & Auth.authResponse>> {
        const token = localStorage.getItem('accessToken');
        return await AxiosJwtInstance.post('/api/v1/auth/signout', {
            headers: {
                Authorization: 'Bearer ${token}'
            }
        });
    }

    async authroize(
        path: string
    ): Promise<AxiosResponse<Boolean>> {
        const token = localStorage.getItem('accessToken');
        return await AxiosJwtInstance.post('/api/v1/auth/authroize', JSON.stringify({ path }), {
            headers: {
                Authorization: 'Bearer ${token}'
            }
        });
    }
}

export default new AuthService();

