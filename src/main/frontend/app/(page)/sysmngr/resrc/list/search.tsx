'use client'

import { useRef, useEffect, forwardRef, useImperativeHandle } from 'react'
import { Button } from "@/components/ui/button"
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select"
import { resrcService } from '@/components/service/Resrc/ResrcService'
import { responseSelectPerPage } from '@/components/entity/responseSelectPerPage'


interface ResourceSearchProps {
    onSearchResults: (results: responseSelectPerPage) => void
}

export const ResourceSearch = forwardRef(
    ({ onSearchResults }: ResourceSearchProps, ref) => {

        const formRef = useRef<HTMLFormElement>(null)

        const handleSearch = async (event?: React.FormEvent<HTMLFormElement>, page:number = 1) => {
            if( event ) event.preventDefault(); // 기본 동작 방지
            const formData = new FormData(formRef.current!); // FormData 객체 생성
            formData.append("page", page.toString());
            const results = await resrcService.search(formData); // 검색 서비스 호출
            console.log( results );
            onSearchResults(results); // 결과 전달
        }

        useImperativeHandle(ref, ()=> ({
            handleSearch,
        }));

        useEffect(() => {
            // 컴포넌트가 마운트될 때 기본 검색 실행
            handleSearch(undefined, 1);
        }, []);

        return (
            <form ref={formRef} onSubmit={(e) => handleSearch(e)} className="flex gap-2 mb-4">
                <Select defaultValue="all" name="searchType">
                    <SelectTrigger className="w-[200px]">
                        <SelectValue placeholder="" />
                    </SelectTrigger>
                    <SelectContent>
                        <SelectItem value="all">전체</SelectItem>
                        <SelectItem value="resrcNm">리소스명</SelectItem>
                        <SelectItem value="resrcUrl">리소스URL</SelectItem>
                    </SelectContent>
                </Select>
                <input
                    type="text"
                    name="searchText"
                    className="flex h-9 w-[300px] rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background"
                    placeholder="검색어를 입력하세요"
                />
                <Button className="bg-green-600 hover:bg-green-700">검색</Button>
            </form>
        )
    }
);

