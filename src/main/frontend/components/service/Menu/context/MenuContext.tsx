'use client'
import React, { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { menuService } from '@/components/service/Menu/MenuService';
import { usePathname } from 'next/navigation';
import { userMenu } from '@/components/entity/MenuInfo';

interface MenuContextType {
  topMenuItem: userMenu | null;
  userMenuItems: userMenu[];
  menuTitle: string;
  currentMenuSn: number | 0;
  setTopMenuItem: React.Dispatch<React.SetStateAction<userMenu | null>>;
  setUserMenuItems: React.Dispatch<React.SetStateAction<userMenu[]>>;
  setMenuTitle: React.Dispatch<React.SetStateAction<string>>;
  setCurrentMenuSn: React.Dispatch<React.SetStateAction<number>>;
}

const MenuContext = createContext<MenuContextType | undefined>(undefined);

export const MenuProvider = ({ children }: { children: ReactNode }) => {
  const pathname = usePathname();
  const [topMenuItem, setTopMenuItem] = useState<userMenu | null>(null);
  const [userMenuItems, setUserMenuItems] = useState<userMenu[]>([]);
  const [menuTitle, setMenuTitle] = useState<string>('');
  const [currentMenuSn, setCurrentMenuSn] = useState<number>(0);

  useEffect(() => {
    const fetchMenus = async () => {
      try {
        const responseUserMenu = await menuService.selectUserMenu(pathname);
        const topMenu = responseUserMenu.topMenu;
        const currentMenu = responseUserMenu.currentMenu;
        const userMenus = responseUserMenu.userMenus;

        setUserMenuItems(userMenus);

        setTopMenuItem(topMenu ?? null);
        setMenuTitle(topMenu ? topMenu.menuNm : '');
        setCurrentMenuSn( currentMenu ? currentMenu.menuSn : 0);

      } catch (error) {
        console.error('Failed to fetch menus:', error);
        setTopMenuItem(null);
        setUserMenuItems([]);
        setMenuTitle('');
        setCurrentMenuSn(0);
      }
    };

    fetchMenus();
  }, [pathname]);

  return (
    <MenuContext.Provider value={{ topMenuItem, userMenuItems, menuTitle, currentMenuSn, setTopMenuItem, setUserMenuItems, setMenuTitle, setCurrentMenuSn}}>
      {children}
    </MenuContext.Provider>
  );
};

export const useMenuContext = () => {
  const context = useContext(MenuContext);
  if (!context) {
    throw new Error('useMenuContext must be used within a MenuProvider');
  }
  return context;
};