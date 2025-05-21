import { useFlightStore } from '@/store/useFlightStore';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';

export const ResultsPage = () => {
    const results = useFlightStore((state) => state.results);
    const navigate = useNavigate();

    if (results.length === 0) {
        return (
            <div className="text-center mt-10">
                <p>No flight results available.</p>
                <Button onClick={() => navigate('/')}>Back to Search</Button>
            </div>
        );
    }

    return (
        <div className="max-w-4xl mx-auto py-8 space-y-4">
            <h2 className="text-2xl font-bold mb-6">Flight Results</h2>

            {results.map((flight) => (
                <Card key={flight.uuid}>
                    <CardHeader>
                        <CardTitle>
                            {flight.departureAirportCode} → {flight.arrivalAirportCode} — {flight.totalPrice}
                        </CardTitle>
                    </CardHeader>
                    <CardContent className="space-y-1 text-sm">
                        <p>
                            <strong>From:</strong> {flight.departureAirportName} ({flight.departureAirportCode})
                        </p>
                        <p>
                            <strong>To:</strong> {flight.arrivalAirportName} ({flight.arrivalAirportCode})
                        </p>
                        <p>
                            <strong>Departure:</strong> {flight.departureTime}
                        </p>
                        <p>
                            <strong>Arrival:</strong> {flight.arrivalTime}
                        </p>
                        <p>
                            <strong>Duration:</strong> {flight.duration}
                        </p>
                        <p>
                            <strong>Stops:</strong> {flight.stops === 0 ? 'Non-stop' : `${flight.stops} stop(s)`}
                        </p>
                        <p>
                            <strong>Airline:</strong> {flight.airlineName} ({flight.airlineCode})
                            {flight.operatingAirlineName && flight.operatingAirlineName !== flight.airlineName
                                ? `, Operated by ${flight.operatingAirlineName} (${flight.operatingAirlineCode})`
                                : ''}
                        </p>
                        <p>
                            <strong>Price (total):</strong> {flight.totalPrice}
                        </p>
                        <p>
                            <strong>Price per traveler:</strong> {flight.pricePerTraveler}
                        </p>

                        <Button
                            onClick={() => navigate(`/details/${flight.uuid}`)}
                            className="mt-2"
                        >
                            View Details
                        </Button>
                    </CardContent>
                </Card>
            ))}
        </div>
    );
};

export default ResultsPage;
