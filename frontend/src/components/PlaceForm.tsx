import { useEffect, useState } from 'react';
import { Category, Place, PlacePayload } from '../types';

interface Props {
  categories: Category[];
  initial?: Place | null;
  onSubmit: (payload: PlacePayload) => Promise<void>;
  onCancel: () => void;
}

export const PlaceForm: React.FC<Props> = ({ categories, initial, onSubmit, onCancel }) => {
  const [formState, setFormState] = useState<PlacePayload>({
    name: '',
    description: '',
    latitude: 0,
    longitude: 0,
    categoryId: categories[0]?.id ?? 0
  });
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (initial) {
      setFormState({
        name: initial.name,
        description: initial.description ?? '',
        latitude: initial.latitude,
        longitude: initial.longitude,
        categoryId: initial.categoryId
      });
    } else {
      setFormState({
        name: '',
        description: '',
        latitude: 0,
        longitude: 0,
        categoryId: categories[0]?.id ?? 0
      });
    }
  }, [categories, initial]);

  const handleChange = (
    event: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = event.target;
    setFormState((prev) => ({
      ...prev,
      [name]: name === 'categoryId' ? Number(value) : value
    }));
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setIsSubmitting(true);
    try {
      await onSubmit({
        ...formState,
        name: formState.name.trim(),
        description: formState.description?.trim() || undefined,
        latitude: Number(formState.latitude),
        longitude: Number(formState.longitude)
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form className="space-y-4" onSubmit={handleSubmit}>
      <div className="grid gap-4 sm:grid-cols-2">
        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700" htmlFor="name">
            Name
          </label>
          <input
            id="name"
            name="name"
            value={formState.name}
            onChange={handleChange}
            required
            className="w-full rounded-md border border-slate-300 px-3 py-2 text-sm shadow-sm focus:border-primary focus:ring-2 focus:ring-primary-light"
          />
        </div>
        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700" htmlFor="categoryId">
            Category
          </label>
          <select
            id="categoryId"
            name="categoryId"
            value={formState.categoryId}
            onChange={handleChange}
            required
            className="w-full rounded-md border border-slate-300 px-3 py-2 text-sm shadow-sm focus:border-primary focus:ring-2 focus:ring-primary-light"
          >
            {categories.map((category) => (
              <option key={category.id} value={category.id}>
                {category.name}
              </option>
            ))}
          </select>
        </div>
      </div>
      <div>
        <label className="mb-1 block text-sm font-medium text-slate-700" htmlFor="description">
          Description
        </label>
        <textarea
          id="description"
          name="description"
          value={formState.description ?? ''}
          onChange={handleChange}
          rows={3}
          className="w-full rounded-md border border-slate-300 px-3 py-2 text-sm shadow-sm focus:border-primary focus:ring-2 focus:ring-primary-light"
        />
      </div>
      <div className="grid gap-4 sm:grid-cols-2">
        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700" htmlFor="latitude">
            Latitude
          </label>
          <input
            id="latitude"
            name="latitude"
            type="number"
            step="any"
            value={formState.latitude}
            onChange={handleChange}
            required
            className="w-full rounded-md border border-slate-300 px-3 py-2 text-sm shadow-sm focus:border-primary focus:ring-2 focus:ring-primary-light"
          />
        </div>
        <div>
          <label className="mb-1 block text-sm font-medium text-slate-700" htmlFor="longitude">
            Longitude
          </label>
          <input
            id="longitude"
            name="longitude"
            type="number"
            step="any"
            value={formState.longitude}
            onChange={handleChange}
            required
            className="w-full rounded-md border border-slate-300 px-3 py-2 text-sm shadow-sm focus:border-primary focus:ring-2 focus:ring-primary-light"
          />
        </div>
      </div>
      <div className="flex items-center gap-3">
        <button
          type="submit"
          disabled={isSubmitting || categories.length === 0}
          className="rounded-md bg-primary px-4 py-2 text-sm font-semibold text-white shadow hover:bg-primary-dark disabled:cursor-not-allowed disabled:opacity-60"
        >
          {isSubmitting ? 'Saving...' : initial ? 'Update Place' : 'Create Place'}
        </button>
        <button
          type="button"
          onClick={onCancel}
          className="rounded-md border border-slate-300 px-4 py-2 text-sm font-semibold text-slate-600 hover:bg-slate-100"
        >
          Cancel
        </button>
      </div>
    </form>
  );
};
