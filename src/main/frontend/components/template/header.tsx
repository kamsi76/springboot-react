'use client'
import { Button } from "@/components/ui/button"
import { LogIn, LogOut } from 'lucide-react'
import { useRouter } from "next/navigation"
import { useMenuContext } from "../service/Menu/context/MenuContext"

export default function Header() {

  const router = useRouter();
  const {userMenuItems} = useMenuContext();
  
  return (
    <header className="flex items-center justify-between h-16 px-4 w-full border-b bg-background w-full z-10 absolute top-0 left-0">
      <div className="flex items-center">
        <h1 className="ml-4 text-lg font-semibold">My App</h1>

        <nav className="absolute text-xl left-1/2 transform -translate-x-1/2">
          <ul className="flex space-x-4">
            {userMenuItems.map((item) => (
                <li key={item.menuSn}>
                  <Button 
                      variant="ghost" 
                      className="text-base"
                      onClick={() => router.push(item.realUrl)}
                      >{item.menuNm}</Button>
                </li>
            ))}
          </ul>
        </nav>
      </div>
      <div className="flex items-right">
        <Button variant="ghost" size="icon" aria-label="Log out" title="Log out">
          <LogOut className="h-5 w-5" />
        </Button>
        <Button
          variant="ghost"
          size="icon"
          aria-label="Log in"
          title="Log in"
          onClick={() => router.push('/auth/signin')}>
          <LogIn className="h-5 w-5" />
        </Button>
      </div>
    </header>
  )
}

