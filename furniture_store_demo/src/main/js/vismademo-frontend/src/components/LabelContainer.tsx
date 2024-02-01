import { useMemo } from "react";
import "../styles/label-container.css";

export interface LabelContainer {
  label: string;
  children: React.ReactNode;
  side?: "left" | "right";
  className?: string;
}

export const LabelContainer = ({
  label,
  children,
  side = "left",
  className,
}: LabelContainer) => {
  const _className: string = useMemo(() => {
    const classNames: string[] = ["label-container"];

    if (side === "right") {
      classNames.push("label-container-right");
    }

    if (className) {
      classNames.push(className);
    }

    return classNames.join(" ");
  }, [side, className]);
  return (
    <label className={_className} title={label}>
      <span>{label}</span>
      {children}
    </label>
  );
};
