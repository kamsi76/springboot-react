import { NextConfig } from 'next';

const nextConfig: NextConfig = {
  reactStrictMode: false, // 필요시 활성화
  publicDir: 'public',
  swcMinify: false, // Next.js의 SWC 사용

  async rewrites() {
    return [
      {
        source: '/api/:path*', // 프론트엔드 요청 경로
        destination: 'http://localhost:8080/api/:path*', // 백엔드 실제 경로
      },
    ];
  },

  webpack: (config, { isServer }) => {
    // Webpack 설정 확장
    if (!isServer) {
      config.devtool = false; // Source Map 설정
    }

    return config;
  },
};

export default nextConfig;
