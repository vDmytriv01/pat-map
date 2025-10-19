import React from 'react';

export const PageContainer: React.FC<{ title: string; actions?: React.ReactNode; children: React.ReactNode }> = ({
  title,
  actions,
  children
}) => {
  return (
    <section className="mx-auto w-full max-w-6xl px-4 py-8">
      <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <h1 className="text-3xl font-semibold text-slate-900">{title}</h1>
        {actions}
      </div>
      <div className="rounded-lg bg-white p-6 shadow-sm ring-1 ring-slate-200">{children}</div>
    </section>
  );
};
