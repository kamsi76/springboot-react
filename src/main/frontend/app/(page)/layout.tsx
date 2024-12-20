import { Inter } from 'next/font/google'
import "../globals.css"
import { SidebarProvider } from "@/components/ui/sidebar"
import Header from "@/components/template/header"
import AppSidebar from '@/components/template/sidebar'
import PrivateRoute from '@/components/common/router/PrivateRouter'
import { MenuProvider } from '@/components/service/Menu/context/MenuContext'

const inter = Inter({ subsets: ["latin"] })

export default function PageLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <SidebarProvider>
      <div className="relative flex flex-col h-screen w-full">
        <div className="flex flex-1">
          <AppSidebar />
          <main className="flex-1 overflow-y-auto w-full px-4">
            <div className="container mx-auto py-6 pt-16">
              {children}
            </div>
          </main>
        </div>
      </div>
    </SidebarProvider>
  )
}

