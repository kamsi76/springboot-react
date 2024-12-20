import axios from "axios";
import CommonError from "../error/CommonError";

const AxiosJwtInstance = axios.create({
    withCredentials: true, // 쿠키를 포함하거나 인증 정보 필요 시
    // timeout: 5000,
    headers: {
        'Content-Type': 'application/json'
    }
});

AxiosJwtInstance.interceptors.request.use(
    (config) => {

        if (typeof window !== 'undefined') {

            /**
             * access token과 refresh token이 있는 경우
             * 정보를 header에 담아서 back-end로 전송한다.
             */
            const accessToken = localStorage.getItem("accessToken") || '';
            const refreshToken = localStorage.getItem('refreshToken') || '';

            if( accessToken && refreshToken ) {
                config.headers['Authorization'] = `Bearer ${accessToken}`;
                config.headers['x-refresh-token'] = `Bearer ${refreshToken}`;
            }
        }

        return config;
    },
    (error) => {
        console.error('[+] 요청중 오류가 발생하였습니다.', error);
        return Promise.reject(error);

    }
);

AxiosJwtInstance.interceptors.response.use(
    (response) => {

        // console.log( response );

        const status = response.status;
        const responseData = response?.data;
        const responseStatus = responseData?.status;
       
        if( status === 200 ) {

            /**
             * 정상적인 데이터가 아닌 경우 모두 response.data에 status 정보를 담고 있다.
             * (이부분은 조금 더 고민이 필요한 부분)
             * status 가 200이 아니면 모두 문제가 있는 오류로 판단한다.
             * 401 경우
             *  access token에 문제가 있는 경우에 발생
             *  back-end에서 refresh token을 검증하고 access token을 반환한다.
             *  반환된 access token을 가지고 다시한번 서버에 로그인 여부를 호출한다.
             *  반환된 access token이 없으면 로그인 페이지로 이동한다.
             * 403 경우
             *  서버 접근 권한이 없는 오류로 경고창을 띄우고 history back 처리 한다.
             * 400 경우
             *  refresh token에 문제가 발생한 것으로 무조건 로그인 화면으로 이동한다.
             * 나머지는 모두 오류 처리 한다.
             */
            if( responseStatus && responseStatus !== 200 ) {

                /**
                 * 401이면서 access token 있으면  access token 재생성한 데이터가 넘어왔기 때문에 다시 서버에 전송 처리
                 * access token이 없으면 로그인 실패로 처리 해서 다시 로그인 처리 화면으로 이동
                 */
                if( responseStatus === 401 ) {
                    if( responseData?.data?.accessToken ) {
                        
                        localStorage.setItem('accessToken', response.data.data.accessToken);

                        const responseConfig = {
                            ...response.config,
                            headers: {
                                ...response.config.headers,
                                Authorization: response.data.data.accessToken,
                            },
                        };

                        

                        return AxiosJwtInstance(responseConfig);
                    } else {
                        window.location.href = '/auth/signin';
                    }
                } else if( responseStatus === 403 ) {
                    // 권한이 없음 메시지 발생시키고 뒤로 이동한다.
                    // 권한이 없을 때 뒤로 이동하는 방법 적용 필요....
                    window.location.href = '/error?code=403'
                } else {
                    throw new CommonError(responseData);
                }
            }

            return response;
        }

        throw new CommonError(responseData);

    },
	(error) => {
		console.log('[-] 응답이 실패한 경우 수행이 됩니다. ', error);
		//window.location.href = '/error?code=' + error.status;
		//return Promise.reject(error);
	}
)

export default AxiosJwtInstance;