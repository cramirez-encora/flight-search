'use client'

import { format } from 'date-fns'
import { Calendar as CalendarIcon } from 'lucide-react'

import { cn } from '@/lib/utils'
import { Button } from '@/components/ui/button'
import { Calendar } from '@/components/ui/calendar'
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from '@/components/ui/popover'
import { Label } from '@/components/ui/label'

function parseLocalDate(dateString: string): Date {
    const [year, month, day] = dateString.split('-').map(Number);
    return new Date(year, month - 1, day); // ✅ This avoids UTC shift
}

interface PinkDatePickerProps {
    label: string
    date?: string
    onChange: (formattedDate: string) => void
    placeholder?: string
}

export function PinkDatePicker({
                                   label,
                                   date,
                                   onChange,
                                   placeholder = "Pick a date",
                               }: PinkDatePickerProps) {
    const localDate = date ? parseLocalDate(date) : undefined;

    return (
        <div className="space-y-1">
            <Label>{label}</Label>
            <Popover>
                <PopoverTrigger asChild>
                    <Button
                        variant="outline"
                        className={cn(
                            "w-full justify-between text-left font-normal",
                            !localDate && "text-muted-foreground"
                        )}
                    >
                        <span>{localDate ? format(localDate, 'PPP') : placeholder}</span>
                        <CalendarIcon className="h-4 w-4 text-pink-600" />
                    </Button>
                </PopoverTrigger>
                <PopoverContent className="w-auto p-0">
                    <Calendar
                        mode="single"
                        selected={localDate}
                        onSelect={(d) => {
                            if (d) {
                                const formatted = d.toLocaleDateString('en-CA') // stays correct
                                onChange(formatted)
                            }
                        }}
                        initialFocus
                    />
                </PopoverContent>
            </Popover>
        </div>
    )
}