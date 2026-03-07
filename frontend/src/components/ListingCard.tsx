import React from 'react';
import type { Listing } from '../types';

interface ListingCardProps {
  listing: Listing;
  onMouseEnter?: () => void;
  onMouseLeave?: () => void;
}

function formatPrice(price: number): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: 'USD',
    maximumFractionDigits: 0,
  }).format(price);
}

const ListingCard: React.FC<ListingCardProps> = ({ listing, onMouseEnter, onMouseLeave }) => {
  return (
    <article
      className="listing-card"
      onMouseEnter={onMouseEnter}
      onMouseLeave={onMouseLeave}
    >
      <div className="listing-card-image">
        {listing.imageUrl ? (
          <img src={listing.imageUrl} alt={listing.address} />
        ) : (
          <div className="listing-card-image-fallback">No photo</div>
        )}
      </div>
      <div className="listing-card-body">
        <p className="listing-card-address">{listing.address}</p>
        <p className="listing-card-price">{formatPrice(listing.price)}</p>
        <p className="listing-card-details">
          {listing.bedrooms} bed · {listing.bathrooms} bath
        </p>
        <div className="listing-card-scores">
          <span className="listing-card-score" title="Nearest dog park rating">
            ★ {listing.nearestDogParkRating.toFixed(1)}
          </span>
          <span className="listing-card-score" title="Distance to dog park">
            {listing.distanceToDogPark.toFixed(1)} mi to park
          </span>
          {listing.nearestDogParkName && (
            <span className="listing-card-park">{listing.nearestDogParkName}</span>
          )}
        </div>
      </div>
    </article>
  );
};

export default ListingCard;
