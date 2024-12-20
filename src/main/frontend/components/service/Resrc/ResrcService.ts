import { AxiosResponse } from "axios";
import AxiosJwtInstance from "../../common/instance/AxiosJwtInstance";
import { responseSelectPerPage } from "@/components/entity/responseSelectPerPage";

class ResrcService {

    // API 호출
    async search(
        searchData: FormData
    ): Promise<responseSelectPerPage> {
        console.log( searchData );
        const response: AxiosResponse<responseSelectPerPage> = await AxiosJwtInstance.post('/api/v1/sysmngr/resrc/list', searchData);
        console.log( response.data);
        return response.data;
    };

}

export const resrcService = new ResrcService();