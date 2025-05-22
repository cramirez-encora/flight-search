import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { format, parseISO } from 'date-fns';
import { Button } from '@/components/ui/button';
import { Card } from '@/components/ui/card';
import { Badge } from '@/components/ui/badge';

interface Amenity {
    name: string;
    chargeable: boolean;
}

interface Segment {
    departureTime: string;
    arrivalTime: string;
    departureAirport: string;
    departureIata: string;
    arrivalAirport: string;
    arrivalIata: string;
    airlineCode: string;
    airlineName: string;
    flightNumber: string;
    operatingAirlineCode: string | null;
    operatingAirlineName: string | null;
    aircraftType: string;
    cabin: string;
    travelClass: string;
    amenities: Amenity[];
}

interface Layover {
    airportName: string;
    airportCode: string;
    duration: string;
}

interface Fee {
    type: string;
    amount: string;
}

interface PriceBreakdown {
    basePrice: string;
    totalPrice: string;
    fees: Fee[];
    pricePerTraveler: string;
}

interface FlightDetailsResponse {
    segments: Segment[];
    layovers: Layover[];
    priceBreakdown: PriceBreakdown;
}

export const FlightDetailsPage = () => {
    const { uuid } = useParams();
    const navigate = useNavigate();
    const [data, setData] = useState<FlightDetailsResponse | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        const fetchDetails = async () => {
            try {
                const res = await fetch(`/api/flights/details/${uuid}`);
                if (!res.ok) throw new Error('Not found');
                const json = await res.json();
                setData(json);
            } catch (err) {
                setError('Could not fetch flight details.');
            } finally {
                setLoading(false);
            }
        };
        fetchDetails();
    }, [uuid]);

    const formatTime = (iso: string) => format(parseISO(iso), 'yyyy-MM-dd HH:mm');

    if (loading) return <p className="text-center mt-10">Loading...</p>;
    if (error || !data)
        return (
            <div className="text-center mt-10">
                <p className="text-red-600">{error}</p>
                <Button onClick={() => navigate('/')}>Back to Search</Button>
            </div>
        );

    return (
        <div className="max-w-7xl mx-auto py-8 px-4 ">
            <Button
                onClick={() => navigate(-1)}
                variant="outline"
                className="border-pink-600 hover:bg-pink-700 hover:text-white mb-6"
            >
                {"<-"} Back to Results
            </Button>

            <div className="flex text-3xl font-extrabold justify-center mb-8">
                Flight Details
            </div>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div className="md:col-span-2 space-y-4">
                    {data.segments.map((segment, index) => (
                        <div key={index}>
                            <Card className="p-4 flex flex-col md:flex-row gap-4 justify-between border-pink-200 shadow-sm bg-pink-50/20">
                                <div className="md:w-2/3 space-y-2">
                                    <div className="text-sm font-bold text-pink-600">Segment {index + 1}</div>
                                    <div className="text-md font-semibold">
                                        {formatTime(segment.departureTime)} - {formatTime(segment.arrivalTime)}
                                    </div>
                                    <div className="text-lg">
                                        <span className="font-semibold">
                                            {segment.departureAirport} ({segment.departureIata})
                                        </span>{' '} → {' '}
                                        <span className="font-semibold">
                                            {segment.arrivalAirport} ({segment.arrivalIata})
                                        </span>
                                    </div>
                                    <div className="text-md text-gray-700 mt-6">
                                        <span className="font-semibold text-pink-600">{segment.airlineName}</span> ({segment.airlineCode})
                                        <div>
                                            <span className={"text-black font-semibold"}>Flight number: </span> {segment.flightNumber}
                                        </div>
                                        {segment.operatingAirlineName && (
                                            <>
                                                {' '}
                                                <span className="italic">
                                                operated by {segment.operatingAirlineName} ({segment.operatingAirlineCode})
                                                </span>
                                            </>
                                        )}
                                    </div>
                                    <div>
                                        <span className="font-semibold">Aircraft Type:</span> {segment.aircraftType}
                                    </div>
                                </div>

                                <div className="md:w-1/3 border p-3 rounded space-y-2 border-pink-300 bg-pink-50 break-words ">
                                    <div className="font-semibold text-pink-600">Traveler Fare Details</div>
                                    <div>Cabin: <span className="font-medium">{segment.cabin}</span></div>
                                    <div>Class: <span className="font-medium">{segment.travelClass}</span></div>
                                    <div>
                                        <div >Amenities:</div>
                                        <div className="flex flex-wrap gap-2">
                                            {segment.amenities.map((amenity, i) => (
                                                <Badge
                                                    key={i}
                                                    variant="outline"
                                                    className="bg-white text-black border border-pink-300 text-sm font-normal px-3 py-1 leading-snug break-words whitespace-normal min-h-[2.25rem] max-w-xs w-full sm:w-fit"
                                                >
                                                    <div className="w-full break-words text-left">
                                                        {amenity.name}{' '}
                                                        <span className="text-pink-600 font-medium">
                                                            {amenity.chargeable ? '(Chargeable)' : '(Free)'}
                                                        </span>
                                                    </div>
                                                </Badge>
                                            ))}
                                        </div>


                                    </div>
                                </div>
                            </Card>

                            {index < data.layovers.length && (
                                <div className="text-center italic text-black py-2 font-bold">
                                    ✈️ Layover at {data.layovers[index].airportName} ({data.layovers[index].airportCode}) — {data.layovers[index].duration}
                                </div>
                            )}
                        </div>
                    ))}
                </div>


                <div className="md:col-span-1 space-y-4">
                    <Card className="p-4 space-y-2 border-pink-200 shadow-sm bg-pink-50/20">
                        <div className="text-lg font-bold text-pink-600 mb-2">Price Breakdown</div>
                        <div>Base Price: <span className="font-medium">${data.priceBreakdown.basePrice}</span></div>
                        <div className="font-semibold mt-2 underline">Fees:</div>
                        {data.priceBreakdown.fees.map((fee, idx) => (
                            <div key={idx}>
                                {fee.type}: <span className="font-medium">${fee.amount}</span>
                            </div>
                        ))}
                        <div className="font-bold pt-2 border-t border-pink-200 text-lg text-pink-600">
                            Total: ${data.priceBreakdown.totalPrice}
                        </div>

                        <Card className="p-3 mt-3 border-pink-200 bg-white">
                            <div className="text-sm font-semibold text-gray-500">Price per Traveler:</div>
                            <div className="text-md font-bold">${data.priceBreakdown.pricePerTraveler}</div>
                        </Card>
                    </Card>
                </div>
            </div>
        </div>
    );
};

export default FlightDetailsPage;
