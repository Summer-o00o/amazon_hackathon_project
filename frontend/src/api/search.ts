import type { SearchResponse } from '../types';

export async function searchListings(query: string): Promise<SearchResponse> {
  const res = await fetch('/api/search', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ query }),
  });

  if (!res.ok) {
    const text = await res.text();
    throw new Error(text || `Search failed: ${res.status}`);
  }

  return res.json();
}
