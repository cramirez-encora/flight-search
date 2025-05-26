import { type FlightSearchResponse, useFlightStore } from '@/store/useFlightStore';
import { useNavigate } from 'react-router-dom';
import { Label } from '@/components/ui/label';
import { Button } from '@/components/ui/button';
import { FlightCard } from '@/components/FlightCard';
import { RoundTripFlightCard } from '@/components/RoundTripFlightCard';
import { useState } from 'react';
import {
    Select,
    SelectTrigger,
    SelectValue,
    SelectContent,
    SelectItem,
} from '@/components/ui/select';
import { ArrowUp, ArrowDown } from 'lucide-react';
import { cn } from '@/lib/utils';

const sortFlights = async (
    results: FlightSearchResponse[],
    sortBy: 'price' | 'duration',
    sortOrder: 'asc' | 'desc'
): Promise<FlightSearchResponse[]> => {
    const response = await fetch('/api/flights/sort', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ results, sortBy, sortOrder }),
    });

    if (!response.ok) {
        throw new Error('Failed to sort flights');
    }

    return response.json();
};

export const ResultsPage = () => {
    const results = useFlightStore((state) => state.results);
    const setResults = useFlightStore((state) => state.setResults);
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [sortBy, setSortBy] = useState<'price' | 'duration'>('price');
    const [sortOrder, setSortOrder] = useState<'asc' | 'desc'>('asc');

    if (results.length === 0) {
        return (
            <div className="text-center mt-10">
                <p>No flight results available.</p>
                <Button onClick={() => navigate('/')}>Back to Search</Button>
            </div>
        );
    }

    const handleSort = async (
        field: 'price' | 'duration',
        order: 'asc' | 'desc'
    ) => {
        setSortBy(field);
        setSortOrder(order);
        setLoading(true);
        setError('');
        try {
            const sorted = await sortFlights(results, field, order);
            setResults(sorted);
        } catch (err) {
            setError('Failed to sort flights');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-4xl mx-auto py-8 space-y-4">
            <Button
                onClick={() => navigate('/')}
                variant="outline"
                className="border-pink-600 hover:bg-pink-700 hover:text-white"
            >
                Return to Search
            </Button>

            <h2 className="text-2xl font-bold mb-4">Flight Results</h2>

            <div className="flex items-center justify-between mb-6">
                <div></div>
                <div className="flex items-center gap-4 ml-auto">
                    <Label>Sort By</Label>
                    <Select
                        value={sortBy}
                        onValueChange={(value) => {
                            const newSortBy = value as 'price' | 'duration';
                            setSortBy(newSortBy);
                            handleSort(newSortBy, sortOrder);
                        }}
                    >
                        <SelectTrigger className="w-[140px] border-pink-600">
                            <SelectValue placeholder="Sort by" />
                        </SelectTrigger>
                        <SelectContent>
                            <SelectItem value="price">Price</SelectItem>
                            <SelectItem value="duration">Duration</SelectItem>
                        </SelectContent>
                    </Select>

                    <div className="flex gap-1">
                        <Button
                            size="icon"
                            variant="outline"
                            disabled={loading}
                            onClick={() => handleSort(sortBy, 'asc')}
                            className={cn(
                                'border-pink-600',
                                sortOrder === 'asc' && 'bg-pink-600 text-white hover:bg-pink-700'
                            )}
                        >
                            <ArrowUp className="h-4 w-4" />
                        </Button>
                        <Button
                            size="icon"
                            variant="outline"
                            disabled={loading}
                            onClick={() => handleSort(sortBy, 'desc')}
                            className={cn(
                                'border-pink-600',
                                sortOrder === 'desc' && 'bg-pink-600 text-white hover:bg-pink-700'
                            )}
                        >
                            <ArrowDown className="h-4 w-4" />
                        </Button>
                    </div>
                </div>
            </div>

            {error && <p className="text-red-500">{error}</p>}

            {results.map((flight) => {
                const key = flight.uuid ?? crypto.randomUUID();
                const isRoundTrip = Boolean(flight.returnDepartureTime);
                return isRoundTrip ? (
                    <RoundTripFlightCard key={key} flights={[flight]} />
                ) : (
                    <FlightCard key={key} flights={[flight]} />
                );
            })}
        </div>
    );
};

export default ResultsPage;
