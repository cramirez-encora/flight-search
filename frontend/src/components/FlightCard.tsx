import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import type { FlightSearchResponse } from '@/store/useFlightStore';
import { useNavigate } from 'react-router-dom';

interface Props {
    flight: FlightSearchResponse;
}

export function FlightCard({ flight }: Props) {
    const navigate = useNavigate();

    return (
        <Card className="mb-4 px-4 py-3">
            <CardContent className="flex justify-between items-start gap-4 p-0">
                <div className="flex-1 flex flex-col justify-between min-h-[100px]">
                    <div className="space-y-2">
                        <div className="text-sm">
                            {flight.departureTime} - {flight.arrivalTime}
                        </div>

                        <div className="font-semibold">
                            {flight.departureAirportName} ({flight.departureAirportCode}) -{' '}
                            {flight.arrivalAirportName} ({flight.arrivalAirportCode})
                        </div>

                        <div className="text-pink-600 font-medium">
                            {flight.duration}{' '}
                            {flight.stops === 0
                                ? '(Nonstop)'
                                : `(${flight.stops} stop${flight.stops > 1 ? 's' : ''})`}
                        </div>
                    </div>

                    <div className="text-sm mt-4 text-gray-700 ">
                        {flight.airlineName} ({flight.airlineCode})
                        {flight.operatingAirlineName &&
                            flight.operatingAirlineCode &&
                            flight.airlineCode !== flight.operatingAirlineCode &&
                            ` · Operated by ${flight.operatingAirlineName} (${flight.operatingAirlineCode})`}
                    </div>
                </div>

                <div className="text-right min-w-[140px] space-y-1">
                    <div>
                        <div className="font-bold text-sm">{flight.totalPrice}</div>
                        <div className="text-xs text-muted-foreground">total</div>
                    </div>

                    <div>
                        <div className="font-bold text-sm">{flight.pricePerTraveler}</div>
                        <div className="text-xs text-muted-foreground">per Traveler</div>
                    </div>
                </div>
            </CardContent>

            <div className="flex justify-end mt-2">
                <Button
                    onClick={() => navigate(`/details/${flight.uuid}`)}
                    variant="pink"
                >
                    View Details
                </Button>
            </div>
        </Card>
    );
}
