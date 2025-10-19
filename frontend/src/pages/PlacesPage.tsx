import { useEffect, useMemo, useState } from 'react';
import toast from 'react-hot-toast';
import { fetchCategories } from '../api/categories';
import { createPlace, deletePlace, fetchPlaces, updatePlace } from '../api/places';
import { Category, Place, PlacePayload } from '../types';
import { PageContainer } from '../components/PageContainer';
import { PlaceForm } from '../components/PlaceForm';

export const PlacesPage: React.FC = () => {
  const [places, setPlaces] = useState<Place[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [selectedCategory, setSelectedCategory] = useState<number | 'all'>('all');
  const [isLoading, setIsLoading] = useState(true);
  const [showForm, setShowForm] = useState(false);
  const [editingPlace, setEditingPlace] = useState<Place | null>(null);

  const loadPlaces = async (categoryId?: number) => {
    setIsLoading(true);
    try {
      const data = await fetchPlaces(categoryId);
      setPlaces(data);
    } catch (error) {
      toast.error('Failed to load places.');
      console.error(error);
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    const init = async () => {
      try {
        const [categoryData] = await Promise.all([fetchCategories()]);
        setCategories(categoryData);
        await loadPlaces();
      } catch (error) {
        toast.error('Failed to initialize data.');
        console.error(error);
      }
    };
    init();
  }, []);

  useEffect(() => {
    if (selectedCategory === 'all') {
      void loadPlaces();
    } else {
      void loadPlaces(selectedCategory);
    }
  }, [selectedCategory]);

  const handleCreate = async (payload: PlacePayload) => {
    await toast.promise(createPlace(payload), {
      loading: 'Creating place...',
      success: 'Place created.',
      error: 'Failed to create place.'
    });
    await loadPlaces(selectedCategory === 'all' ? undefined : selectedCategory);
    setShowForm(false);
  };

  const handleUpdate = async (payload: PlacePayload) => {
    if (!editingPlace) return;
    await toast.promise(updatePlace(editingPlace.id, payload), {
      loading: 'Updating place...',
      success: 'Place updated.',
      error: 'Failed to update place.'
    });
    await loadPlaces(selectedCategory === 'all' ? undefined : selectedCategory);
    setShowForm(false);
    setEditingPlace(null);
  };

  const handleDelete = async (place: Place) => {
    const confirmed = window.confirm(`Delete place "${place.name}"?`);
    if (!confirmed) return;

    await toast.promise(deletePlace(place.id), {
      loading: 'Deleting place...',
      success: 'Place deleted.',
      error: 'Failed to delete place.'
    });
    setPlaces((prev) => prev.filter((item) => item.id !== place.id));
  };

  const formTitle = useMemo(() => (editingPlace ? `Edit ${editingPlace.name}` : 'Create place'), [editingPlace]);

  return (
    <PageContainer
      title="Places"
      actions={
        <button
          onClick={() => {
            setShowForm(true);
            setEditingPlace(null);
          }}
          className="rounded-md bg-primary px-4 py-2 text-sm font-semibold text-white shadow hover:bg-primary-dark"
        >
          New Place
        </button>
      }
    >
      <div className="mb-6 flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between">
        <label className="text-sm font-medium text-slate-600">
          Filter by category
          <select
            value={selectedCategory}
            onChange={(event) =>
              setSelectedCategory(event.target.value === 'all' ? 'all' : Number(event.target.value))
            }
            className="ml-3 rounded-md border border-slate-300 px-3 py-2 text-sm shadow-sm focus:border-primary focus:ring-2 focus:ring-primary-light"
          >
            <option value="all">All</option>
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
        </label>
      </div>

      {showForm && (
        <div className="mb-6 rounded-lg border border-slate-200 p-4">
          <div className="mb-4 flex items-center justify-between">
            <h2 className="text-lg font-semibold text-slate-800">{formTitle}</h2>
            <button className="text-sm text-slate-500 hover:text-slate-700" onClick={() => setShowForm(false)}>
              Close
            </button>
          </div>
          <PlaceForm
            categories={categories}
            initial={editingPlace}
            onSubmit={editingPlace ? handleUpdate : handleCreate}
            onCancel={() => {
              setShowForm(false);
              setEditingPlace(null);
            }}
          />
        </div>
      )}

      {isLoading ? (
        <p className="text-sm text-slate-600">Loading places...</p>
      ) : (
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-slate-200">
            <thead className="bg-slate-50">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-600">
                  Name
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-600">
                  Category
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-600">
                  Coordinates
                </th>
                <th className="px-4 py-3 text-left text-xs font-semibold uppercase tracking-wider text-slate-600">
                  Description
                </th>
                <th className="px-4 py-3 text-right text-xs font-semibold uppercase tracking-wider text-slate-600">
                  Actions
                </th>
              </tr>
            </thead>
            <tbody className="divide-y divide-slate-100 bg-white">
              {places.map((place) => (
                <tr key={place.id}>
                  <td className="whitespace-nowrap px-4 py-3 text-sm font-medium text-slate-800">{place.name}</td>
                  <td className="px-4 py-3 text-sm text-slate-600">{place.categoryName ?? categories.find((c) => c.id === place.categoryId)?.name ?? '—'}</td>
                  <td className="px-4 py-3 text-sm text-slate-600">
                    {place.latitude.toFixed(4)}, {place.longitude.toFixed(4)}
                  </td>
                  <td className="px-4 py-3 text-sm text-slate-600">{place.description ?? '—'}</td>
                  <td className="flex justify-end gap-2 px-4 py-3 text-sm">
                    <button
                      onClick={() => {
                        setEditingPlace(place);
                        setShowForm(true);
                      }}
                      className="rounded-md border border-slate-300 px-3 py-1 text-xs font-medium text-slate-600 hover:bg-slate-100"
                    >
                      Edit
                    </button>
                    <button
                      onClick={() => handleDelete(place)}
                      className="rounded-md border border-red-200 bg-red-50 px-3 py-1 text-xs font-medium text-red-600 hover:bg-red-100"
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
          {places.length === 0 && (
            <p className="px-4 py-6 text-center text-sm text-slate-500">No places found. Add a place to populate your map.</p>
          )}
        </div>
      )}
    </PageContainer>
  );
};
