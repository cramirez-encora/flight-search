import React, { useEffect, useState } from "react"
import {
    Command,
    CommandEmpty,
    CommandGroup,
    CommandInput,
    CommandItem
} from "./ui/command"
import {
    Popover,
    PopoverTrigger,
    PopoverContent
} from "./ui/popover"
import { useFlightSearchStore } from "@/store/useFlightSearchStore"
import type { AirportOption } from "@/store/useFlightSearchStore"
import { Button } from "./ui/button"

type Props = {
    label: string
    type: "origin" | "destination"
}

export const AirportSearchInput: React.FC<Props> = ({ label, type }) => {
    const [open, setOpen] = useState(false)
    const [inputValue, setInputValue] = useState("")
    const [options, setOptions] = useState<AirportOption[]>([])

    const { setField, origin, destination } = useFlightSearchStore()
    const selected = type === "origin" ? origin : destination

    // Fetch matching airports
    useEffect(() => {
        const controller = new AbortController()
        const delay = setTimeout(() => {
            if (inputValue.trim().length > 0) {
                fetch(`/api/airports/search?keyword=${inputValue}`, {
                    signal: controller.signal
                })
                    .then((res) => res.json())
                    .then((data) => setOptions(data))
                    .catch((err) => {
                        if (err.name !== "AbortError") {
                            console.error(err)
                            setOptions([])
                        }
                    })
            } else {
                setOptions([])
            }
        }, 300)

        return () => {
            controller.abort()
            clearTimeout(delay)
        }
    }, [inputValue])

    const handleSelect = (airport: AirportOption) => {
        setField(type, airport)
        setOpen(false)
        setInputValue(`${airport.name} (${airport.iataCode})`) // ✅ keep input filled
    }

    const displayText = selected ? `${selected.name} (${selected.iataCode})` : "Search airport..."

    return (
        <div className="w-full">
            <label className="block mb-1 font-medium">{label}</label>
            <Popover open={open} onOpenChange={setOpen}>
                <PopoverTrigger asChild>
                    <Button
                        variant="outline"
                        role="combobox"
                        className="w-full justify-between"
                        onClick={() => {
                            setOpen(true)
                            if (!inputValue && selected) {
                                setInputValue(`${selected.name} (${selected.iataCode})`)
                            }
                        }}
                    >
                        {displayText}
                    </Button>
                </PopoverTrigger>
                <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
                    <Command>
                        <CommandInput
                            placeholder="Search airport..."
                            value={inputValue}
                            onValueChange={setInputValue}
                        />
                        <CommandEmpty>No airports found.</CommandEmpty>
                        <CommandGroup>
                            {options.map((airport) => (
                                <CommandItem
                                    key={`${airport.iataCode}-${airport.name}`}
                                    value={`${airport.iataCode}-${airport.name}`}
                                    onSelect={() => handleSelect(airport)}
                                >
                                    {airport.name} ({airport.iataCode}) — {airport.city}, {airport.country}
                                </CommandItem>
                            ))}
                        </CommandGroup>
                    </Command>
                </PopoverContent>
            </Popover>
        </div>
    )
}
