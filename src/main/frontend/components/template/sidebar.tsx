'use client'
import React, { useEffect, useState } from 'react'
import { ChevronDown, ChevronRight } from 'lucide-react'
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarMenu,
  SidebarMenuItem,
  SidebarMenuButton,
  SidebarMenuSub,
  SidebarMenuSubItem,
  SidebarMenuSubButton,
} from '@/components/ui/sidebar'
import { Collapsible, CollapsibleContent, CollapsibleTrigger } from '@/components/ui/collapsible';
import { userMenu } from '../entity/MenuInfo'

import { useMenuContext } from "../service/Menu/context/MenuContext"

// 현재 선택된 메뉴의 상태를 확인하여 열기


const RenderMenuItem: React.FC<{ item: userMenu; selectedMenu: number; setSelectedMenu: React.Dispatch<React.SetStateAction<number>>; }> = ({ item, selectedMenu, setSelectedMenu }) => {

  const [isOpen, setIsOpen] = useState(false);

  useEffect(() => {
    // 선택된 메뉴가 하위 메뉴에 포함되어 있는지 재귀적으로 확인
    const isSelectedMenuInSubItems = (menu: userMenu): boolean => {
      if (menu.menuSn === selectedMenu) return true;
      return menu.subMenus?.some(isSelectedMenuInSubItems) ?? false;
    };

    setIsOpen(isSelectedMenuInSubItems(item));
  }, [selectedMenu, item]);

  return (
    <Collapsible open={isOpen} onOpenChange={setIsOpen}>
      <SidebarMenuItem>
        <CollapsibleTrigger asChild>
          <SidebarMenuButton onClick={() => setIsOpen(!isOpen)} className={item.menuSn === selectedMenu ? "bg-accent text-accent-foreground" : ""}>
            <span>{item.menuNm}</span>
            {item.subMenus ? (
              isOpen ? (
                <ChevronDown className="ml-auto h-4 w-4" />
              ) : (
                <ChevronRight className="ml-auto h-4 w-4" />
              )
            ) : null}
          </SidebarMenuButton>
        </CollapsibleTrigger>
        {item.subMenus && (
          <CollapsibleContent>
            <SidebarMenuSub>
              {item.subMenus.map((subItem) => (
                <SidebarMenuSubItem key={subItem.menuSn}>
                  {subItem.subMenus ? (
                    <RenderMenuItem item={subItem} selectedMenu={selectedMenu} setSelectedMenu={setSelectedMenu} />
                  ) : (
                    <SidebarMenuSubButton asChild className={subItem.menuSn === selectedMenu ? "bg-accent text-accent-foreground" : ""}>
                      <a href={subItem.realUrl} onClick={() => setSelectedMenu(subItem.menuSn)}>{subItem.menuNm}</a>
                    </SidebarMenuSubButton>
                  )}
                </SidebarMenuSubItem>
              ))}
            </SidebarMenuSub>
          </CollapsibleContent>
        )}
      </SidebarMenuItem>
    </Collapsible>
  );
};

export default function SidebarComponent() {
  const [selectedMenu, setSelectedMenu] = useState<number>(0); // 메뉴관리의 menuSn
  const {topMenuItem, menuTitle, currentMenuSn} = useMenuContext();
  const menuItems = topMenuItem?.subMenus || [];

  useEffect(() => {
    setSelectedMenu(currentMenuSn);
  }, [currentMenuSn]);

  return (
    <Sidebar>
      <SidebarContent>
        <SidebarGroup>
          <SidebarGroupLabel>{menuTitle}</SidebarGroupLabel>
          <SidebarGroupContent>
            <SidebarMenu>
              {menuItems.map((item) => (
                <RenderMenuItem key={item.menuSn} item={item} selectedMenu={selectedMenu} setSelectedMenu={setSelectedMenu} />
              ))}
            </SidebarMenu>
          </SidebarGroupContent>
        </SidebarGroup>
      </SidebarContent>
    </Sidebar>
  )
}

