'use client'

import { useState, useRef, useEffect } from 'react'
import { Pagination } from '@/components/common/pagination'
import { Button } from "@/components/ui/button"
import { resrcInfo } from '@/components/entity/ResrcInfo'
import { ResourceSearch } from './search'
import { ResourceTable } from './table'
import { responseSelectPerPage } from '@/components/entity/responseSelectPerPage'

export default function ResourceManagement() {
    const [resources, setResources] = useState<resrcInfo[]>([])
    const [page, setCurrentPage] = useState(1)
    const [totalRecord, setTotalRecord] = useState(0)
    const searchRef = useRef<{ handleSearch: (event?: React.FormEvent<HTMLFormElement>, page?: number) => void }>(null);

    const handleSearchResults = (results: responseSelectPerPage) => {
        setResources(results.data);
        setTotalRecord(results.search.totalRecord);
    }

    const handleEdit = (resource: resrcInfo) => {
        console.log('Edit resource:', resource)
    }

    const handlePageChange = (page: number) => {
        setCurrentPage(page); // 현재 페이지 상태 업데이트
        console.log(`Current page changed to: ${page}`);
        console.log('searchRef.current', searchRef.current);
        searchRef.current?.handleSearch(undefined, page);
    };

    useEffect(() => {
        console.log('Total record updated:', totalRecord);
    }, [totalRecord]);

    return (
        <div>
            <div className="flex items-center justify-between mb-6">
                <h1 className="text-2xl font-semibold">리소스관리</h1>
                <div className="flex items-center gap-2">
                    <span className="text-sm text-muted-foreground">
                        관리자정보 {'>'} 메뉴 및 권한관리 {'>'} 리소스관리
                    </span>
                </div>
            </div>

            <ResourceSearch ref={searchRef} onSearchResults={handleSearchResults} />
            <ResourceTable resources={resources} onEdit={handleEdit} />
            <Pagination
                currentPage={page}
                totalRecord={totalRecord}
                recordPerPage={5}
                onPageChange={handlePageChange}
            />

            <div className="flex justify-end mt-4">
                <Button className="bg-gray-600 hover:bg-gray-700">등록</Button>
            </div>
        </div>
    )
}

