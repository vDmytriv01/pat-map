import { useEffect, useState } from 'react';
import { Category, CategoryPayload } from '../types';

interface Props {
  initial?: Category | null;
  onSubmit: (payload: CategoryPayload) => Promise<void>;
  onCancel: () => void;
}

export const CategoryForm: React.FC<Props> = ({ initial, onSubmit, onCancel }) => {
  const [formState, setFormState] = useState<CategoryPayload>({ name: '', description: '' });
  const [isSubmitting, setIsSubmitting] = useState(false);

  useEffect(() => {
    if (initial) {
      setFormState({ name: initial.name, description: initial.description ?? '' });
    } else {
      setFormState({ name: '', description: '' });
    }
  }, [initial]);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>) => {
    const { name, value } = event.target;
    setFormState((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setIsSubmitting(true);
    try {
      await onSubmit({
        name: formState.name.trim(),
        description: formState.description?.trim() || undefined
      });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <form className="space-y-4" onSubmit={handleSubmit}>
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
        <label className="mb-1 block text-sm font-medium text-slate-700" htmlFor="description">
          Description
        </label>
        <textarea
          id="description"
          name="description"
          value={formState.description}
          onChange={handleChange}
          rows={3}
          className="w-full rounded-md border border-slate-300 px-3 py-2 text-sm shadow-sm focus:border-primary focus:ring-2 focus:ring-primary-light"
        />
      </div>
      <div className="flex items-center gap-3">
        <button
          type="submit"
          disabled={isSubmitting}
          className="rounded-md bg-primary px-4 py-2 text-sm font-semibold text-white shadow hover:bg-primary-dark disabled:cursor-not-allowed disabled:opacity-60"
        >
          {isSubmitting ? 'Saving...' : initial ? 'Update Category' : 'Create Category'}
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
