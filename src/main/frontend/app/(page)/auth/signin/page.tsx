'use client'

import React, {useRef} from "react";
import Image from 'next/image'
import Link from 'next/link'

import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'

import { useRouter } from 'next/navigation'

import { authService } from "@/components/service/Auth/AuthService";
import { requestUser } from "@/components/entity/Auth";
import { fnFormValidataion } from "@/components/common/validator/validateFrom";

export default function Login() {

  const router = useRouter()
  const formRef = useRef<HTMLFormElement>(null);
  
  async function handleSubmit(event: React.FormEvent<HTMLFormElement>) {
    event.preventDefault(); // 기본 제출 방지

    try {
      const isValid = fnFormValidataion(formRef);

      if( isValid ) {

        const formData = new FormData(event.currentTarget);
        const userId = formData.get("userId") as string;
        const passwd = formData.get("passwd") as string; 

        if( userId === null ) {
          throw new Error("userId is required!");
        }

        const authUser : requestUser = {
          userId: userId,
          passwd: passwd
        }

        console.log('[+] 로그인 수행');
        const res = await authService.signin(authUser);
        console.log('[+] 로그인 성공 ', res);
        const result = res.data;
        localStorage.setItem('accessToken', result.data.accessToken);
        localStorage.setItem('refreshToken', result.data.refreshToken);

        debugger;
        router.push("/dashboard");

      }
    } catch (error) {
      console.error("[-] Error : " + error );
      throw new Error("로그인 실패하였습니다.");
    }

    return false;
  }

  return (
    <div className="flex h-screen w-screen flex-col items-center justify-center bg-gray-100">
      <div className="w-full max-w-md space-y-8 rounded-xl bg-white p-10 shadow-xl">
        <div className="flex flex-col items-center space-y-2">
          <Image
            src="/logo.png"
            alt="Logo"
            width={64}
            height={64}
            className="h-16 w-16"
          />
          <h2 className="text-3xl font-bold">로그인</h2>
          <p className="text-sm text-gray-500">계정에 로그인하세요</p>
        </div>
        <form className="space-y-6" ref={formRef} onSubmit={handleSubmit}>
          <div className="space-y-2">
            <Label htmlFor="email">아이디</Label>
            <Input name="userId" type="text" title="아이디" required />
          </div>
          <div className="space-y-2">
            <Label htmlFor="password">비밀번호</Label>
            <Input name="passwd" type="password" required />
          </div>
          <div className="flex items-center justify-between">
            <div className="flex items-center">
              <input
                id="remember-me"
                name="remember-me"
                type="checkbox"
                className="h-4 w-4 rounded border-gray-300 text-blue-600 focus:ring-blue-500"
              />
              <Label htmlFor="remember-me" className="ml-2 block text-sm text-gray-900">
                로그인 상태 유지
              </Label>
            </div>
            <div className="text-sm">
              <Link href="/forgot-password" className="font-medium text-blue-600 hover:text-blue-500">
                비밀번호를 잊으셨나요?
              </Link>
            </div>
          </div>
          <Button type="submit" className="w-full">
            로그인
          </Button>
        </form>
        <div className="text-center text-sm">
          계정이 없으신가요?{' '}
          <Link href="/signup" className="font-medium text-blue-600 hover:text-blue-500">
            회원가입
          </Link>
        </div>
      </div>
    </div>
  )
}