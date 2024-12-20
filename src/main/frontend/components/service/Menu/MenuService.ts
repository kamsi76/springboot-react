import { AxiosResponse } from "axios";
import AxiosJwtInstance from "../../common/instance/AxiosJwtInstance";
import { responseUserMenu } from "../../entity/MenuInfo";

class MenuService {

    // API 호출
    async selectUserMenu(
        currentPath: string
    ): Promise<responseUserMenu> {
        console.log( currentPath );
        const response: AxiosResponse<responseUserMenu> = await AxiosJwtInstance.post('/api/v1/sysmngr/menu/selectUserMenu', JSON.stringify({currentPath}));
        return response.data;
    };

}

export const menuService = new MenuService();