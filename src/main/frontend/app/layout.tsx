'use client';

import './globals.css'
import { Inter } from 'next/font/google'
import PrivateRoute from '@/components/common/router/PrivateRouter';

const inter = Inter({ subsets: ['latin'] })

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
      <html lang="en">
        <body className={inter.className}>
        <PrivateRoute>
          {children}
        </PrivateRoute>
        </body>
      </html>
  )
}

