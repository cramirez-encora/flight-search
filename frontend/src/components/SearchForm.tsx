import React from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import {PinkDatePicker} from "@/components/ui/datepicker.tsx";
import { Label } from '@/components/ui/label';
import { Checkbox } from '@/components/ui/checkbox';
import {
    Select,
    SelectTrigger,
    SelectContent,
    SelectItem,
    SelectValue,
} from '@/components/ui/select';
import { AirportSearchInput } from '@/components/AirportSearchInput';
import { useFlightSearchStore } from '@/store/useFlightSearchStore';

export const SearchForm = () => {
    const {
        origin,
        destination,
        departureDate,
        returnDate,
        currencyCode,
        adults,
        nonStop,
        setField,
    } = useFlightSearchStore();


    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const requestPayload = {
            originLocationCode: origin?.iataCode || "",
            destinationLocationCode: destination?.iataCode || "",
            departureDate,
            returnDate,
            adults,
            currencyCode,
            nonStop,
        };

        console.log("Flight search payload as JSON:", JSON.stringify(requestPayload, null, 2));

        try {
            const res = await fetch('/api/flights/search', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(requestPayload),
            });

            if (!res.ok) {
                const errorText = await res.text();
                throw new Error(`Request failed with status ${res.status}: ${errorText}`);
            }

            const data = await res.json();
            console.log("Flight search response:", data);

        } catch (error) {
            console.error('Error during flight search:', error);
        }
    };

    return (
        <form className="space-y-4 max-w-xl mx-auto" onSubmit={handleSubmit}>
            <AirportSearchInput label="Departure Airport" type="origin" />
            <AirportSearchInput label="Arrival Airport" type="destination" />

            <div className="grid grid-cols-2 gap-4">
                <PinkDatePicker
                    label="Departure Date"
                    date={departureDate}
                    onChange={(value) => setField("departureDate", value)}
                />
                <PinkDatePicker
                    label="Return Date"
                    date={returnDate}
                    onChange={(value) => setField("returnDate", value)}
                />
            </div>


            <div className="grid grid-cols-2 gap-4">
                <div>
                    <Label>Adults</Label>
                    <Input
                        type="number"
                        min={1}
                        value={adults}
                        onChange={(e) => setField('adults', parseInt(e.target.value))}
                    />
                </div>
                <div>
                    <Label>Currency</Label>
                    <Select
                        value={currencyCode}
                        onValueChange={(v) => setField('currencyCode', v as any)}
                    >
                        <SelectTrigger>
                            <SelectValue placeholder="Select currency" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="USD">USD</SelectItem>
                            <SelectItem value="MXN">MXN</SelectItem>
                            <SelectItem value="EUR">EUR</SelectItem>
                        </SelectContent>
                    </Select>
                </div>
            </div>

            <div className="flex items-center space-x-2">
                <Checkbox
                    id="nonStop"
                    variant="pink"
                    checked={nonStop}
                    onCheckedChange={(checked) => setField('nonStop', Boolean(checked))}
                />

                <Label htmlFor="nonStop">Non-stop flights only</Label>
            </div>

            <Button type="submit" className="w-full" variant={"pink"}>
                Search Flights
            </Button>
        </form>
    );
};
