'use client'

import { Button } from "@/components/ui/button"

interface PaginationProps {
    currentPage: number;    //현재 페이지
    totalRecord: number;    //전체 게시물 수
    recordPerPage: number;  //한 페이지당 게시되는 게시물 건수
    onPageChange: (page: number) => void;
}

export function Pagination({ currentPage, totalRecord, recordPerPage, onPageChange }: PaginationProps) {

    const totalPage = Math.ceil(totalRecord / recordPerPage );
    const pages = Array.from({ length: totalPage }, (_, i) => i + 1);

    const handlePageClick = (page: number) => {
        if (page > 0 && page <= totalPage) {
            onPageChange(page);
        }
    }

    return (
        <div className="flex justify-center gap-1 mt-4">
            <button onClick={() => handlePageClick(currentPage - 1)} disabled={currentPage === 1}>
                이전
            </button>
            {pages.map((page) => (
                <Button
                    key={page}
                    variant={currentPage === page ? "default" : "outline"}
                    className="h-8 w-8"
                    onClick={() => onPageChange(page)}
                >
                    {page}
                </Button>
            ))}
            <button
                onClick={() => handlePageClick(currentPage + 1)}
                disabled={currentPage === totalPage}
            >
                다음
            </button>
        </div>
    )
}