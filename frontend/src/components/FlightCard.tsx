import { Card, CardContent, CardHeader } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import type { FlightSearchResponse } from '@/store/useFlightStore';
import { useNavigate } from 'react-router-dom';

interface Props {
    flight: FlightSearchResponse;
}

export function FlightCard({ flight }: Props) {
    const navigate = useNavigate();

    return (
        <Card className="mb-4">
            <CardHeader className="text-lg font-semibold">
                {flight.departureAirportCode} → {flight.arrivalAirportCode} · {flight.duration}
            </CardHeader>
            <CardContent>
                <p>
                    {flight.departureTime} - {flight.arrivalTime}
                </p>
                <p>
                    Airline: {flight.airlineName} ({flight.airlineCode})
                    {flight.operatingAirlineName && flight.operatingAirlineCode &&
                        flight.airlineCode !== flight.operatingAirlineCode &&
                        ` · Operated by ${flight.operatingAirlineName} (${flight.operatingAirlineCode})`}
                </p>
                <p>Stops: {flight.stops}</p>
                <p>Total Price: {flight.totalPrice}</p>
                <p>Price Per Traveler: {flight.pricePerTraveler}</p>
                <Button onClick={() => navigate(`/details/${flight.uuid}`)} className="mt-2">
                    View Details
                </Button>
            </CardContent>
        </Card>
    );
}
