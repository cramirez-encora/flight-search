import React from 'react';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
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
        currency,
        adults,
        nonStop,
        setField,
    } = useFlightSearchStore();

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const requestPayload = {
            originLocationCode: origin?.iataCode || "",
            destinationLocationCode: destination?.iataCode || "",
            departureDate,
            returnDate,
            currency,
            adults,
            nonStop,
        };

        console.log("Flight search request payload:", requestPayload);
    };

    return (
        <form className="space-y-4 max-w-xl mx-auto" onSubmit={handleSubmit}>
            <AirportSearchInput label="Departure Airport" type="origin" />
            <AirportSearchInput label="Arrival Airport" type="destination" />

            <div className="grid grid-cols-2 gap-4">
                <div>
                    <Label>Departure Date</Label>
                    <Input
                        type="date"
                        value={departureDate}
                        onChange={(e) => setField('departureDate', e.target.value)}
                    />
                </div>
                <div>
                    <Label>Return Date</Label>
                    <Input
                        type="date"
                        value={returnDate}
                        onChange={(e) => setField('returnDate', e.target.value)}
                    />
                </div>
            </div>

            <div className="grid grid-cols-2 gap-4">
                <div>
                    <Label>Currency</Label>
                    <Select
                        value={currency}
                        onValueChange={(v) => setField('currency', v as any)}
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
                <div>
                    <Label>Adults</Label>
                    <Input
                        type="number"
                        min={1}
                        value={adults}
                        onChange={(e) => setField('adults', parseInt(e.target.value))}
                    />
                </div>
            </div>

            <div className="flex items-center space-x-2">
                <Checkbox
                    id="nonStop"
                    checked={nonStop}
                    onCheckedChange={(checked) => setField('nonStop', Boolean(checked))}
                />
                <Label htmlFor="nonStop">Non-stop flights only</Label>
            </div>



            <Button type="submit" className="w-full">
                Search Flights
            </Button>
        </form>
    );
};
