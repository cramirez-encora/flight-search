import { Card, CardContent } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import type { FlightSearchResponse } from '@/store/useFlightStore';
import { useNavigate } from 'react-router-dom';

interface Props {
    flights: FlightSearchResponse[]; // [departure, return]
}

export function RoundTripFlightCard({ flights }: Props) {
    const navigate = useNavigate();
    const [departure, returnFlight] = flights;

    return (
        <Card className="mb-4 px-4 py-3">
            <CardContent className="flex flex-col gap-4 p-0">
                {/* Departure Segment */}
                <div className="flex justify-between items-start gap-4">
                    <div className="flex-1 flex flex-col justify-between min-h-[100px]">
                        <div className="space-y-2">
                            <div className="text-sm text-muted-foreground">Departure</div>
                            <div className="text-sm">
                                {departure.departureTime} - {departure.arrivalTime}
                            </div>

                            <div className="font-semibold">
                                {departure.departureAirportName} ({departure.departureAirportCode}) -{' '}
                                {departure.arrivalAirportName} ({departure.arrivalAirportCode})
                            </div>

                            <div className="text-pink-600 font-medium">
                                {departure.duration}{' '}
                                {departure.stops === 0
                                    ? '(Nonstop)'
                                    : `(${departure.stops} stop${departure.stops > 1 ? 's' : ''})`}
                            </div>
                        </div>

                        <div className="text-sm mt-4 text-gray-700">
                            {departure.airlineName} ({departure.airlineCode})
                            {departure.operatingAirlineName &&
                                departure.operatingAirlineCode &&
                                departure.airlineCode !== departure.operatingAirlineCode &&
                                ` · Operated by ${departure.operatingAirlineName} (${departure.operatingAirlineCode})`}
                        </div>
                    </div>

                    <div className="text-right min-w-[140px] space-y-1">
                        <div>
                            <div className="font-bold text-sm">{departure.totalPrice}</div>
                            <div className="text-xs text-muted-foreground">total</div>
                        </div>
                        <div>
                            <div className="font-bold text-sm">{departure.pricePerTraveler}</div>
                            <div className="text-xs text-muted-foreground">per Traveler</div>
                        </div>
                    </div>
                </div>

                {/* Return Segment */}
                {returnFlight && (
                    <div className="flex justify-between items-start gap-4 border-t pt-4">
                        <div className="flex-1 flex flex-col justify-between min-h-[100px]">
                            <div className="space-y-2">
                                <div className="text-sm text-muted-foreground">Return</div>
                                <div className="text-sm">
                                    {returnFlight.departureTime} - {returnFlight.arrivalTime}
                                </div>

                                <div className="font-semibold">
                                    {returnFlight.departureAirportName} ({returnFlight.departureAirportCode}) -{' '}
                                    {returnFlight.arrivalAirportName} ({returnFlight.arrivalAirportCode})
                                </div>

                                <div className="text-pink-600 font-medium">
                                    {returnFlight.duration}{' '}
                                    {returnFlight.stops === 0
                                        ? '(Nonstop)'
                                        : `(${returnFlight.stops} stop${returnFlight.stops > 1 ? 's' : ''})`}
                                </div>
                            </div>

                            <div className="text-sm mt-4 text-gray-700">
                                {returnFlight.airlineName} ({returnFlight.airlineCode})
                                {returnFlight.operatingAirlineName &&
                                    returnFlight.operatingAirlineCode &&
                                    returnFlight.airlineCode !== returnFlight.operatingAirlineCode &&
                                    ` · Operated by ${returnFlight.operatingAirlineName} (${returnFlight.operatingAirlineCode})`}
                            </div>
                        </div>

                        <div className="text-right min-w-[140px] space-y-1">
                            <div>
                                <div className="font-bold text-sm">{returnFlight.totalPrice}</div>
                                <div className="text-xs text-muted-foreground">total</div>
                            </div>
                            <div>
                                <div className="font-bold text-sm">{returnFlight.pricePerTraveler}</div>
                                <div className="text-xs text-muted-foreground">per Traveler</div>
                            </div>
                        </div>
                    </div>
                )}
            </CardContent>

            <div className="flex justify-end mt-2">
                <Button
                    onClick={() => navigate(`/details/${departure.uuid}`)}
                    variant="pink"
                >
                    View Details Round
                </Button>
            </div>
        </Card>
    );
}
