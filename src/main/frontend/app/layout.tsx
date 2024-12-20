import { Inter } from 'next/font/google'
import "./globals.css"
import Header from "@/components/template/header"
import PrivateRoute from '@/components/common/router/PrivateRouter'
import { MenuProvider } from '@/components/service/Menu/context/MenuContext'

const inter = Inter({ subsets: ["latin"] })

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <PrivateRoute>
          <div className="relative flex flex-col h-screen w-full">
          <MenuProvider>
            <Header />
            <div className="flex flex-1">
              <main className="flex-1 overflow-y-auto w-full">
                {children}
              </main>
            </div>
            </MenuProvider>
          </div>
        </PrivateRoute>
      </body>
    </html>
  )
}

