export interface menuInfo {
    menuSn: number;
    upperMenuSn?: number | null;
    resrcSn: number | null;
    menuNm: string;
    menuUrl: string | null;
    menuLevel: number;
    popupYn: string;
    useYn: string;
    outptYn?: string | null;
    menuOrd: number;
    registerId: string | null;
    registDttm: Date | null;
    updusrId: string | null;
    updtDttm?: Date | null;
}


export interface menuRole {
    menuSn: number;
    roleCd: string | null;
    roleNm: string | null;
    readAuthYn: string | null;
    writeAuthYn: string | null;
}

export interface userMenu extends menuInfo{
    parent?: userMenu | null;
    subMenus?: userMenu[] | null;
    menuRols?: menuRole[] | null;
    realUrl: string;
}

export interface responseUserMenu {
    topMenu: userMenu | null;
    currentMenu: userMenu | null;
    userMenus: userMenu[];
}