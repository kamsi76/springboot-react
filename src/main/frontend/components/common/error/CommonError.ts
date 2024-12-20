interface ErrorData {
  message: string;
  status?: number;
  data?: object;
}

class CommonError extends Error {
    status?: number;
    data?: object;

    constructor(errorData?: Partial<ErrorData>) {
      super(errorData?.message);  // 부모 클래스인 Error의 생성자를 호출하여 message를 설정합니다.
      this.name = 'CommonError';  // 에러의 이름을 지정합니다.
      this.status = errorData?.status;  // HTTP 상태 코드를 포함시킵니다.
      this.data = errorData?.data;  // 추가적인 응답 데이터를 포함시킵니다.
    }
  }

  export default CommonError;