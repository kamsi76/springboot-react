"use client";

export default function Dashboard() {

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      <div className="bg-white p-8 rounded-lg shadow-md">
        <h1 className="text-2xl font-bold mb-4">대시보드</h1>
        <p className="mb-4">이 페이지는 인증된 사용자만 접근할 수 있습니다.</p>
        <button
          className="bg-red-500 text-white px-4 py-2 rounded hover:bg-red-600"
        >
          로그아웃
        </button>
      </div>
    </div>
  );
}