"use client";

import Link from 'next/link'

export default function Home() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-md">
        <h1 className="text-2xl font-bold mb-4">홈 페이지</h1>
        <p className="mb-4">오류 페이지 입니다.</p>
        <div className="space-x-4">
          <Link href="/dashboard" className="text-blue-500 hover:underline">
            대시보드로 이동
          </Link>
        </div>
      </div>
    </div>
  )
}