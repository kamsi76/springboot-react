module.exports = {
  async rewrites() {
    return [
      {
        source: '/api/:path*', // 프론트엔드 요청 경로
        destination: 'http://localhost:8080/api/:path*', // 백엔드 실제 경로
      },
    ];
  },
};