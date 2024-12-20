'use client'

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import { Button } from "@/components/ui/button"
import { resrcInfo } from "@/components/entity/ResrcInfo"

interface ResourceTableProps {
  resources: resrcInfo[];
  onEdit: (resource: resrcInfo) => void;
}

export function ResourceTable({ resources, onEdit }: ResourceTableProps) {
  return (
    <div className="rounded-md border">
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>리소스명</TableHead>
            <TableHead>리소스URL</TableHead>
            <TableHead className="w-[150px]">접근권한구분</TableHead>
            <TableHead className="w-[80px] text-center">개발구분</TableHead>
            <TableHead className="w-[80px] text-center">순서</TableHead>
            <TableHead className="w-[100px]">사용여부</TableHead>
            <TableHead className="w-[80px]"></TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {resources.map((resource) => (
            <TableRow key={resource.resrcUrl}>
              <TableCell>{resource.resrcNm}</TableCell>
              <TableCell>{resource.resrcUrl}</TableCell>
              <TableCell>{resource.accessTypeCd}</TableCell>
              <TableCell className="text-center">{resource.resrcOrd}</TableCell>
              <TableCell>{resource.resrcUseYn}</TableCell>
              <TableCell>
                <Button 
                  className="bg-blue-600 hover:bg-blue-700 h-8 w-16"
                  onClick={() => onEdit(resource)}
                >
                  수정
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  )
}

