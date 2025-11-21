const Button = ({
  children,
  onClick,
  variant = "primary",
  className = "",
  type = "button",
  disabled,
}) => {
  const baseStyle =
    "px-4 py-2 rounded-md font-medium transition-colors duration-200 flex items-center justify-center gap-2";
  const variants = {
    primary: "bg-blue-600 text-white hover:bg-blue-700 disabled:bg-blue-400",
    secondary:
      "bg-gray-200 text-gray-800 hover:bg-gray-300 disabled:bg-gray-100",
    outline: "border-2 border-blue-600 text-blue-600 hover:bg-blue-50",
    danger: "bg-red-500 text-white hover:bg-red-600",
  };
  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={`${baseStyle} ${variants[variant]} ${className}`}
    >
      {children}
    </button>
  );
};

export default Button;
